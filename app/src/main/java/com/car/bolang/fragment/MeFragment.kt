package com.car.bolang.fragment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.car.bolang.R
import com.car.bolang.activity.*
import com.car.bolang.bean.BannerBean
import com.car.bolang.bean.UserInfoVO
import com.car.bolang.common.BaseActivity
import com.car.bolang.common.BaseFragment
import com.car.bolang.inter.CancelAndSureCallback
import com.car.bolang.inter.ShareAppCallback
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.*
import com.car.bolang.view.CancelAndSureDialog
import kotlinx.android.synthetic.main.fragment_me.*
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXTextObject
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.HashMap


class MeFragment :BaseFragment(),CancelAndSureCallback,ShareAppCallback{


    private var api: IWXAPI? = null
    private val mTargetScene = SendMessageToWX.Req.WXSceneSession

    override fun getLayoutId(): Int {
        return R.layout.fragment_me
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        api = WXAPIFactory.createWXAPI(activity, Constants.WX_APP_ID, true)
    }

    override fun initData() {
        //邀请码
        llInviteCode.setOnClickListener {
            activity?.let {
                ToastUtils.toastShort(activity!!,"本功能暂未开放~")
                return@setOnClickListener
                InviteCodeActivity.startAction(it)
            }
        }

        //会员充值
        llVipRecharge.setOnClickListener {
            activity?.let {
                ToastUtils.toastShort(activity!!,"本功能暂未开放~")
                return@setOnClickListener
                RechargeActivity.startAction(it)
            }
        }

        llModifyPassword.setOnClickListener {
            activity?.let {
                ToastUtils.toastShort(activity!!,"本功能暂未开放~")
                return@setOnClickListener
                val fragment=PaymentFragment()
                fragment.show(it.supportFragmentManager,"sss")
            }
        }

        llInviteFriend.setOnClickListener {
            ToastUtils.toastShort(activity!!,"本功能暂未开放~")
            return@setOnClickListener
            showShareDialog()
        }
        //问题列表
        llProblem.setOnClickListener {
            activity?.let {
                IpSettingActivity.startAction(activity!!)
                return@setOnClickListener

            }
        }
        llLoginOut.setOnClickListener {
            activity?.let {
                ToastUtils.toastShort(it,"本功能暂未开放~")
                return@setOnClickListener
                var dialog=CancelAndSureDialog(it)
                dialog.setCallback(this)
                dialog.setData("提示","是否登出当前账号")
                dialog.show()
            }
        }
    }

    private fun getCardTime(card:String):String{
        if(card.length>10){
            return card.substring(0,10)
        }
        return card
    }
    private fun userName(username:String):String {
        if(TextUtils.isEmpty(username)){
            return ""
        }
        if(username.length>6){
            val stringBuffer=StringBuffer()
            for (i in username.indices){
                if(i in 3..6){
                    stringBuffer.append("*")
                }else{
                    stringBuffer.append(username[i])
                }
            }
            return stringBuffer.toString()
        }
        return username

    }

    private fun showShareDialog(){
        val fragment=ShareAppFragment()
        fragment.setShareCallback(this)
        fragment.show(fragmentManager!!,"hx")
        return

    }

    fun sendMsgToWx(){
        val webpage = WXWebpageObject()
        webpage.webpageUrl = "http://download.laingman.com"
        val msg = WXMediaMessage(webpage)
        msg.title = "铂朗三元催化"
        msg.description =
            "获取最新报价 邀请好友注册还送超值会员权益！"
        val thumb = BitmapFactory.decodeResource(resources, R.drawable.logo)
        msg.thumbData = Util.bmpToByteArray(thumb, true)

        val req = SendMessageToWX.Req()
        req.transaction = buildTransaction("webpage")
        req.message = msg
        req.scene = mTargetScene
        api?.sendReq(req)
    }

    private fun buildTransaction(type: String?): String {
        return if (type == null) System.currentTimeMillis().toString() else type + System.currentTimeMillis()
    }

    override fun onSure() {
        PreferencesUtil.getInstance().clearData()
        LoginActivity.startActionClear(activity!!)
    }

    override fun onCancel() {


    }


    override fun onAppShare() {
        sendMsgToWx()
    }

    fun getUserInfo(){
        val params = HashMap<String, String>()
        NetHelpUtils.okgoGet(activity, UrlProtocol.
            USER_INFO, params, object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz","result${result}")
                result?.let {
                val bean=GsonUtil.GsonToBean(it, UserInfoVO::class.java)
                    bean?.data?.let { data->
                        PreferencesUtil.getInstance().cardTime=data.memberExpireTime
                    }

                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                (activity as BaseActivity).mLoadingDialog?.show()
                ToastUtils.toastShort(activity!!, getString(R.string.network_error))
            }
        })
    }

}