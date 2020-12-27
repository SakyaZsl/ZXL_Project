package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import com.car.bolang.R
import com.car.bolang.bean.SmartRecordReq
import com.car.bolang.common.BaseActivity
import com.car.bolang.fragment.DeviceTypeDialog
import com.car.bolang.inter.ChooseCallBack
import com.car.bolang.util.*
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_register_goods.btnNext
import kotlinx.android.synthetic.main.activity_register_goods.etUserName
import kotlinx.android.synthetic.main.activity_register_goods.etUserNum
import kotlinx.android.synthetic.main.activity_register_goods.etUserPhone
import kotlinx.android.synthetic.main.activity_register_goods.tvDeviceType
import kotlinx.android.synthetic.main.activity_register_goods.tvTime
import kotlinx.android.synthetic.main.activity_register_goods.tvUserClass
import kotlinx.android.synthetic.main.activity_register_goods.tvUserLevel
import kotlinx.android.synthetic.main.activity_register_goods.tvWeek

class RegisterGoodsActivity :BaseActivity() {

    private var mDialog:DeviceTypeDialog?=null
    private var mPosition=-1
    private var mGroupPosition=0
    private var mDeviceList:List<String> ?=null
    private var recordVo:SmartRecordReq ?=null

    companion object {
        fun  startAction(context: Context,position:Int){
            val intent= Intent(context,RegisterGoodsActivity::class.java)
            intent.putExtra(Constants.DEVICE_POSITION,position)
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_register_goods
    }

    override fun init() {
        tvTitle.text="取物品"
        ivBack.setOnClickListener {
            finish()
        }
        mPosition=intent.getIntExtra(Constants.DEVICE_POSITION,0)

        mDeviceList=InitUtils.deviceList[mPosition]

        mDialog= DeviceTypeDialog()
        btnNext.setOnClickListener {
            if(TextUtils.isEmpty(checkInfo())){
                saveRecord()
                return@setOnClickListener
            }
            ToastUtils.toastShort(this,checkInfo())
        }
        tvDeviceType.setOnClickListener {
            mDialog?.setData(mDeviceList!!)
            mDialog?.setCallback(object: ChooseCallBack{
                override fun onChoose(content: String,position: Int) {
                    tvDeviceType.text=content
                }
            })
            mDialog?.show(supportFragmentManager,"hhh")
        }

        tvUserLevel.setOnClickListener {
            mDialog?.setData(InitUtils.classList)
            mDialog?.setCallback(object: ChooseCallBack{
                override fun onChoose(content: String,position: Int) {
                    tvUserLevel.text=content
                    mGroupPosition=position
                    tvUserClass.text=""
                }
            })
            mDialog?.show(supportFragmentManager,"hhh")
        }

        tvUserClass.setOnClickListener {
            mDialog?.setData(InitUtils.groupList[mGroupPosition])
            mDialog?.setCallback(object: ChooseCallBack{
                override fun onChoose(content: String,position: Int) {
                    tvUserClass.text=content
                }
            })
            mDialog?.show(supportFragmentManager,"hhh")
        }
        tvTime.text=TimeUtils.getTime(System.currentTimeMillis(),"yyyy-MM-dd HH:mm")
        tvWeek.text="KW${TimeUtils.getCurrentWeek()}"

    }

    override fun initData() {
    }

    fun checkInfo():String{
        if(TextUtils.isEmpty(tvDeviceType.text)){
            return "请选择设备型号"
        }
        if(TextUtils.isEmpty(tvUserLevel.text)){
            return "请选择工段"
        }
        if(TextUtils.isEmpty(tvUserClass.text)){
            return "请选择班组"
        }
        if(TextUtils.isEmpty(etUserName.text)){
            return "请输入取件人姓名"
        }
        if(TextUtils.isEmpty(etUserPhone.text)){
            return "请输入电话"
        }
        if(TextUtils.isEmpty(etUserNum.text)){
            return "请输入工位号"
        }
        return ""
    }


    private fun saveRecord() {
        ToastUtils.toastShort(this,"开门成功，请选取物品")
        recordVo= SmartRecordReq("0",tvDeviceType.text.toString(),tvUserLevel.text.toString(),tvUserClass.text.toString(),etUserName.text.toString()
            ,etUserPhone.text.toString(),etUserNum.text.toString(), tvTime.text.toString(),tvWeek.text.toString(),""
            ,InitUtils.deviceNameList[mPosition],"","")
        UploadGoodsActivity.startAction(this@RegisterGoodsActivity,recordVo!!,true,mPosition)
    }
}