package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Log
import com.car.bolang.R
import com.car.bolang.bean.BaseBean
import com.car.bolang.bean.BaseVO
import com.car.bolang.bean.SUCCESS_CODE
import com.car.bolang.bean.UpLoadDeviceType
import com.car.bolang.common.BaseActivity
import com.car.bolang.fragment.DeviceTypeDialog
import com.car.bolang.inter.ChooseCallBack
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.*
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_give_back.*
import kotlinx.android.synthetic.main.activity_register_goods.*
import kotlinx.android.synthetic.main.activity_register_goods.btnNext
import kotlinx.android.synthetic.main.activity_register_goods.etUserName
import kotlinx.android.synthetic.main.activity_register_goods.etUserNum
import kotlinx.android.synthetic.main.activity_register_goods.etUserPhone
import kotlinx.android.synthetic.main.activity_register_goods.tvDeviceType
import kotlinx.android.synthetic.main.activity_register_goods.tvTime
import kotlinx.android.synthetic.main.activity_register_goods.tvUserClass
import kotlinx.android.synthetic.main.activity_register_goods.tvUserLevel
import kotlinx.android.synthetic.main.activity_register_goods.tvWeek
import java.util.HashMap

class RegisterGoodsActivity :BaseActivity() {

    private var mDialog:DeviceTypeDialog?=null
    private var mPosition=0
    private var mDeviceList:List<String> ?=null
    private var mClassPosition=-1

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
        if(mPosition<6){
            mDeviceList=InitUtils.deviceList[mPosition]
        }
        mDialog= DeviceTypeDialog()
        btnNext.setOnClickListener {
            if(TextUtils.isEmpty(checkInfo())){
                getGoods()
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
                    mPosition=position
                    tvUserClass.text=""
                }
            })
            mDialog?.show(supportFragmentManager,"hhh")
        }

        tvUserClass.setOnClickListener {
            if(mPosition==-1){
                ToastUtils.toastShort(this,"请先选择班组")
                return@setOnClickListener
            }
            mDialog?.setData(InitUtils.groupList[mPosition])
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


    private fun getGoods(){
        mLoadingDialog?.show()
        val bean=UpLoadDeviceType("1", tvDeviceType.text.toString(),tvUserLevel.text.toString(),tvUserClass.text.toString(),etUserName.text.toString()
                ,etUserPhone.text.toString(),etUserNum.text.toString(),tvTime.text.toString(),TimeUtils.getCurrentWeek().toString(),"")
        NetHelpUtils.okGoBodyPost(this, UrlProtocol.
            TEST_SUBMIT, GsonUtil.GsonString(bean), object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                mLoadingDialog?.dismiss()
                Log.e("zzzz","result${result}")
                val bean=GsonUtil.GsonToBean(result,BaseVO::class.java)
                if (SUCCESS_CODE==bean.code){
                    UploadGoodsActivity.startAction(this@RegisterGoodsActivity)
                    return
                }
                ToastUtils.toastShort(this@RegisterGoodsActivity, getString(R.string.network_error))
            }

            override fun onError(code: Int, errorMsg: String?) {
                mLoadingDialog?.dismiss()
                Log.e("zzzz","result${errorMsg}")
                ToastUtils.toastShort(this@RegisterGoodsActivity, getString(R.string.network_error))
            }
        })
    }
}