package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import com.car.bolang.R
import com.car.bolang.common.BaseActivity
import com.car.bolang.util.Constants
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_ip_setting.*

class IpSettingActivity :BaseActivity(){

    companion object {
        fun  startAction(context: Context){
            val intent= Intent(context,IpSettingActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun setLayoutResID(): Int {
        return R.layout.activity_ip_setting
    }

    override fun init() {
        ivBack.setOnClickListener { finish() }
        tvTitle.text="IP设置"
        etIpAddress.setText(PreferencesUtil.getInstance().ipAddress.toString())
        etIpPort.setText(PreferencesUtil.getInstance().ipPort.toString())
    }

    override fun initData() {
        btnSaveIp.setOnClickListener {
            if(etIpAddress.text.toString().isNullOrEmpty()){
                ToastUtils.toastShort(this,"请输入ip地址")
            }

            if(etIpPort.text.toString().isNullOrEmpty()){
                ToastUtils.toastShort(this,"请输入ip端口")
            }

            PreferencesUtil.getInstance().ipAddress = etIpAddress.text.toString()
            PreferencesUtil.getInstance().ipPort = etIpPort.text.toString()
            finish()
        }
    }
}