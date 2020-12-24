package com.car.bolang.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.car.bolang.R
import com.car.bolang.bean.BaseBean
import com.car.bolang.bean.GoodDetailVO
import com.car.bolang.bean.InviteCodeReq
import com.car.bolang.common.BaseActivity
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ShareConstants
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_invite_code.*
import java.util.HashMap

class InviteCodeActivity : BaseActivity() {
    private var code = ""

    companion object {
        fun startAction(activity: Activity) {
            val intent = Intent(activity, InviteCodeActivity::class.java)
            activity.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_invite_code
    }

    override fun init() {
        tvTitle.text = "邀请码"
        code = PreferencesUtil.getInstance().getParam(ShareConstants.KEY_INVITE_CODE, "").toString()
        if (TextUtils.isEmpty(code)) {
            changeViewNoCode()
        } else {
            changeViewHasCode()
        }
    }

    private fun changeViewHasCode() {
        etInviteCode.visibility = View.GONE
        tvInviteCode.visibility = View.VISIBLE
        ivInviteNext.visibility = View.GONE
        tvInviteState.text = "邀请码"
        tvInviteCode.text = code
    }

    private fun changeViewNoCode() {
        etInviteCode.visibility = View.VISIBLE
        tvInviteCode.visibility = View.GONE
        tvInviteState.text = "请输入邀请码"
        ivInviteNext.visibility = View.VISIBLE
    }

    override fun initData() {
        ivBack.setOnClickListener {
            onBackPressed()
        }
        ivInviteNext.setOnClickListener {
            addInviteInfo()
        }

    }


    private fun addInviteInfo() {
        if (TextUtils.isEmpty(etInviteCode.text)) {
            ToastUtils.toastShort(this, "请输入邀请码")
            return
        }
        mLoadingDialog?.show()
        val userName =
            PreferencesUtil.getInstance().getParam(ShareConstants.KEY_USER_NAME, "").toString()
        val params = InviteCodeReq(etInviteCode.text.toString(), userName)
        NetHelpUtils.okGoBodyPost(
            this,
            UrlProtocol.ADD_INVITE,
            GsonUtil.GsonString(params),
            object :
                HttpUtilsInterface {
                override fun onSuccess(result: String?) {
                    Log.e("zzzzz", "onSuccess result${result}")
                    goToRecharge(this@InviteCodeActivity)
                    mLoadingDialog?.dismiss()
                    result?.let {
                        val bean = GsonUtil.GsonToBean(result, BaseBean::class.java)
                        if (bean.code == 0) {
                            code = etInviteCode.text.toString()
                            PreferencesUtil.getInstance().saveParam(
                                ShareConstants.KEY_INVITE_CODE,
                                etInviteCode.text.toString()
                            )
                            ToastUtils.toastShort(this@InviteCodeActivity, "邀请成功")
                            changeViewHasCode()
                        } else {
                            ToastUtils.toastShort(this@InviteCodeActivity, bean.message)
                        }

                    }


                }

                override fun onError(code: Int, errorMsg: String?) {
                    mLoadingDialog?.dismiss()
                    ToastUtils.toastShort(
                        this@InviteCodeActivity,
                        getString(R.string.network_error)
                    )
                }
            })
    }


}