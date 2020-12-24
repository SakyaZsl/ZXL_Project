package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import com.car.bolang.bean.LoginReqBean
import com.car.bolang.bean.LoginVO
import com.car.bolang.common.BaseActivity
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ShareConstants
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity :BaseActivity(){
    private var time=60

    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }

        fun startActionClear(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return com.car.bolang.R.layout.activity_login
    }

    override fun init() {
        tvSmsCode.setOnClickListener {
            getSmsCode()
        }

        btnLogin.setOnClickListener {
            login()
        }

    }

    override fun initData() {
//        login()

    }




    private  fun login(){
        //手机格式不对
        if(!TextUtils.isEmpty(isPhoneCorrect())){
            ToastUtils.toastShort(this,isPhoneCorrect())
            return
        }
        if(!TextUtils.isEmpty(isCodeCorrect())){
            ToastUtils.toastShort(this,isCodeCorrect())
            return
        }
        mLoadingDialog?.show()
        val params = LoginReqBean(etUserPhone.text.toString(),etUserCode.text.toString(),"")
        NetHelpUtils.okGoBodyPost(this, UrlProtocol.LOGIN, GsonUtil.GsonString(params), object : HttpUtilsInterface{
            override fun onSuccess(result: String?) {
                mLoadingDialog?.dismiss()
                Log.e("zzzz","登录 result${result}")
                result?.let {result->
                  var loginVO=  GsonUtil.GsonToBean(result, LoginVO::class.java)
                    loginVO?.data?.let {
                        PreferencesUtil.getInstance().saveParam(ShareConstants.KEY_TOKEN,it.token)
                        it.user.let { user->
                            PreferencesUtil.getInstance().saveParam(ShareConstants.KEY_INVITE_CODE,user.invitationCode)
                            PreferencesUtil.getInstance().saveParam(ShareConstants.KEY_USER_NAME,user.username)
                            PreferencesUtil.getInstance().saveParam(ShareConstants.KEY_USER_ID,user.id)
                            PreferencesUtil.getInstance().saveParam(ShareConstants.KEY_CARD_NAME,user.grade)
                            PreferencesUtil.getInstance().saveParam(ShareConstants.KEY_CARD_TIME,user.memberExpireTime)
                        }
                        MainActivity.startAction(this@LoginActivity)
                    }?:{
                        ToastUtils.toastShort(this@LoginActivity,loginVO.message)
                    }.invoke()

                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                ToastUtils.toastShort(this@LoginActivity,"请求失败，请重试")
                mLoadingDialog?.dismiss()
            }
        })
    }

    private fun getSmsCode(){
        if(!TextUtils.isEmpty(isPhoneCorrect())){
            ToastUtils.toastShort(this,isPhoneCorrect())
            return
        }
        mHandler.post(runnable)
        val params = HashMap<String,String>()
        NetHelpUtils.okgoGet(this, UrlProtocol.SEND_MSG+"${etUserPhone.text}", params, object : HttpUtilsInterface{
            override fun onSuccess(result: String?) {
                Log.e("zzzz",result)
                ToastUtils.toastShort(this@LoginActivity,"短信验证码发送成功")

            }

            override fun onError(code: Int, errorMsg: String?) {
                Log.e("zzzz",""+errorMsg)
                ToastUtils.toastShort(this@LoginActivity,"短信验证码发送失败，请重试")
            }
        })

    }

    private fun isPhoneCorrect():String{
        if(!isMobileNO(etUserPhone.text.toString())){
            return "电话格式不对，请重新输入"
        }
        return ""
    }

    private fun isCodeCorrect():String{
        if(TextUtils.isEmpty(etUserCode.text)){
            return "验证码不能为空，请重试"
        }
        if(!(etUserCode.text.length==4||etUserCode.text.length==6)){
            return "验证码格式不对，请重试"
        }
        return ""
    }


    /**
     * 验证手机格式
     */
    private fun isMobileNO(mobiles: String): Boolean {
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        val telRegex = "[1][3456789]\\d{9}"
        return if (TextUtils.isEmpty(mobiles))
            false
        else
            mobiles.matches(telRegex.toRegex())
    }

    private var runnable= Runnable {
        time--
        mHandler.sendEmptyMessage(time)

    }

    public fun startRunnable(){
        mHandler.postDelayed(runnable,1000)
    }

    private var mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            if(msg.what>0){
                tvSmsCode.text="${msg.what}s后重新获取"
                tvSmsCode.isEnabled=false
                startRunnable()
            }else{
                tvSmsCode.text="获取验证码"
                tvSmsCode.isEnabled=true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(runnable)
    }

}