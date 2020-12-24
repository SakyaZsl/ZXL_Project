package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.RechargeAdapter
import com.car.bolang.bean.*
import com.car.bolang.common.BaseActivity
import com.car.bolang.fragment.PaymentFragment
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.inter.PaymentCallback
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.Constants
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ToastUtils
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_recharge.*
import java.util.HashMap
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe
import com.car.bolang.event.PaymentCallbackEvent
import org.greenrobot.eventbus.EventBus


/**
 * 充值页面
 */
class RechargeActivity : BaseActivity(), OnItemClickListener, PaymentCallback {
    private var api: IWXAPI? = null

    private var mAdapter: RechargeAdapter? = null
    private var rechargeType = 0
    private var mCurrentBean:MemberRecord ?=null
    private var mDataList: MutableList<MemberRecord> = ArrayList()

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, RechargeActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_recharge
    }

    override fun init() {
        EventBus.getDefault().register(this)
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID)
        ivBack.setOnClickListener { finish() }
        tvTitle.text = "会员充值"
        btnRecharge.setOnClickListener {
            finish()
        }

        mAdapter = RechargeAdapter(this, mDataList)
        mAdapter?.setonItemClickListenr(this)
        rvCardList.adapter = mAdapter
        rvCardList.layoutManager = GridLayoutManager(this, 3)
        btnRecharge.setOnClickListener {
            showPaymentDialog(mCurrentBean!!.amount.toFloat())
        }
        getMemberCardList()
    }

    override fun initData() {
    }

    override fun onItemClick(view: View?, position: Int) {
        mAdapter?.setCurrentPosition(position)
        rechargeType = mDataList[position].sort
        mCurrentBean=mDataList[position]

    }

    private fun showPaymentDialog(pay: Float) {
        val fragment = PaymentFragment()
        fragment.setPayNumber(pay)
        fragment.setPayCallback(this)
        fragment.show(supportFragmentManager, "BL")

    }


    /**
     * 开始充值
     */
    private fun startPayment() {
        mLoadingDialog?.show()
        val map = HashMap<String, String>()
        map["totalFee"] =mCurrentBean?.amount.toString()
        map["attach"] = "会员充值"
        NetHelpUtils.okgoPost(this, UrlProtocol.START_PAYMENT, map, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz", "startPayment${result}")
                result?.let {
                    val bean = GsonUtil.GsonToBean(it, PaymentResultVO::class.java)
                    bean.data?.let { data ->
                        startWxPay(data)
                    }

                } ?: {
                    ToastUtils.toastShort(
                        this@RechargeActivity,
                        getString(com.car.bolang.R.string.payment_error)
                    )
                }.invoke()
                mLoadingDialog?.dismiss()
            }

            override fun onError(code: Int, errorMsg: String?) {
                Log.e("zzzz", "onError${errorMsg}")
                mLoadingDialog?.dismiss()
                ToastUtils.toastShort(
                    this@RechargeActivity,
                    getString(com.car.bolang.R.string.network_error)
                )
            }
        })
    }

    fun startWxPay(bean: PaymentData) {
        val req = PayReq()
        req.appId = bean.appId
        req.partnerId = bean.partnerid
        req.prepayId = bean.prepayid
        req.nonceStr = bean.nonceStr
        req.timeStamp = bean.timeStamp
        req.packageValue = "Sign=WXPay"
        req.sign = bean.signType
        req.extData = "app data" // optional
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api?.sendReq(req)

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onGetStickyEvent(message: PaymentCallbackEvent) {
        Log.e("zzzz", "message${message.payStatus}")
        if(message.payStatus==0){
            rechargeVip()
        }else{
            ToastUtils.toastShort(this,getString(R.string.payment_failed))
        }
    }

    /**
     * 充值
     */
    private fun rechargeVip() {
        mLoadingDialog?.show()
        Log.e("zzzz", "rechargeVip${PreferencesUtil.getInstance().userName}")
        val params =
            GsonUtil.GsonString(RechargeReq(rechargeType, PreferencesUtil.getInstance().userName))
        NetHelpUtils.okGoBodyPost(this, UrlProtocol.RECHARGE_VIP, params, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz", "rechargeVip${result}")
                mLoadingDialog?.dismiss()
                if (result == null) {
                    return
                }
                var bean = GsonUtil.GsonToBean(result, BaseBean::class.java)
                bean?.let {
                    if (it.code == 0) {
                        ToastUtils.toastShort(
                            this@RechargeActivity,
                            getString(com.car.bolang.R.string.recharge_success)
                        )
                    } else {
                        ToastUtils.toastShort(
                            this@RechargeActivity,
                            getString(com.car.bolang.R.string.recharge_fail)
                        )
                    }
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                Log.e("zzzz", "onError${errorMsg}")
                mLoadingDialog?.dismiss()
                ToastUtils.toastShort(
                    this@RechargeActivity,
                    getString(com.car.bolang.R.string.network_error)
                )
            }
        })
    }

    private fun getMemberCardList() {
        mLoadingDialog?.show()
        val params = HashMap<String, String>()
        params["pageNum"] = "0"
        params["pageSize"] = "10"
        NetHelpUtils.okgoGet(this, UrlProtocol.MEMBER_CARD_LIST, params, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                mLoadingDialog?.dismiss()
                if (result == null) {
                    ToastUtils.toastShort(
                        this@RechargeActivity,
                        getString(com.car.bolang.R.string.network_error)
                    )
                    return
                }
                var bean = GsonUtil.GsonToBean(result, MemberListVO::class.java)
                bean?.data?.records?.let {
                    if (it.isNotEmpty()) {
                        mDataList.clear()
                        mDataList.addAll(it)
                        mCurrentBean=mDataList[0]
                        mAdapter?.notifyDataSetChanged()
                    }
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                mLoadingDialog?.dismiss()
                ToastUtils.toastShort(
                    this@RechargeActivity,
                    getString(com.car.bolang.R.string.network_error)
                )
            }
        })
    }

    override fun startPayment(pay: Float) {
        startPayment()
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}