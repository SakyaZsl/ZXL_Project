package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.car.bolang.R
import com.car.bolang.bean.BannerBean
import com.car.bolang.bean.BaseBean
import com.car.bolang.bean.BaseBeanVO
import com.car.bolang.bean.SmartRecordReq
import com.car.bolang.common.BaseActivity
import com.car.bolang.fragment.DeviceTypeDialog
import com.car.bolang.inter.ChooseCallBack
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.*
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_give_back.*
import kotlinx.android.synthetic.main.activity_give_back.tvWeek
import kotlinx.android.synthetic.main.activity_register_goods.*
import kotlinx.android.synthetic.main.activity_register_goods.btnNext
import kotlinx.android.synthetic.main.activity_register_goods.etUserName
import kotlinx.android.synthetic.main.activity_register_goods.etUserNum
import kotlinx.android.synthetic.main.activity_register_goods.etUserPhone
import kotlinx.android.synthetic.main.activity_register_goods.tvDeviceType
import kotlinx.android.synthetic.main.activity_register_goods.tvTime
import kotlinx.android.synthetic.main.activity_register_goods.tvUserClass
import kotlinx.android.synthetic.main.activity_register_goods.tvUserLevel
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.HashMap

class GiveBackActivity : BaseActivity() {

    private var mDialog: DeviceTypeDialog? = null
    private var mPosition = 0
    private var mGroupPosition = 0
    private var mDeviceList: List<String>? = null
    private var mReasonPosition = -1
    private var recordVo: SmartRecordReq? = null

    companion object {
        fun startAction(context: Context, position: Int) {
            val intent = Intent(context, GiveBackActivity::class.java)
            intent.putExtra(Constants.DEVICE_POSITION, position)
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_give_back
    }

    override fun init() {
        tvTitle.text = "还物品"
        ivBack.setOnClickListener {
            finish()
        }
        mPosition = intent.getIntExtra(Constants.DEVICE_POSITION, 0)
        mDeviceList = InitUtils.deviceList[mPosition]
        if(mPosition==5){
            tvDeviceNum.visibility=View.GONE
            etDeviceNum.visibility=View.VISIBLE
        }else{
            tvDeviceNum.visibility=View.VISIBLE
            etDeviceNum.visibility=View.GONE
            tvDeviceNum.text=InitUtils.deviceNumList[mPosition]+"-"
        }
        mDialog = DeviceTypeDialog()
        btnNext.setOnClickListener {
            if (TextUtils.isEmpty(checkInfo())) {
                saveRecord()
                return@setOnClickListener
            }
            ToastUtils.toastShort(this, checkInfo())
        }
        tvDeviceType.setOnClickListener {
            mDialog?.setData(mDeviceList!!)
            mDialog?.setCallback(object : ChooseCallBack {
                override fun onChoose(content: String, position: Int) {
                    tvDeviceType.text = content
                }
            })
            mDialog?.show(supportFragmentManager, "hhh")
        }

        tvUserLevel.setOnClickListener {
            mDialog?.setData(InitUtils.classList)
            mDialog?.setCallback(object : ChooseCallBack {
                override fun onChoose(content: String, position: Int) {
                    tvUserLevel.text = content
                    mGroupPosition = position
                    tvUserClass.text = ""
                }
            })
            mDialog?.show(supportFragmentManager, "hhh")
        }

        tvUserClass.setOnClickListener {
            mDialog?.setData(InitUtils.groupList[mGroupPosition])
            mDialog?.setCallback(object : ChooseCallBack {
                override fun onChoose(content: String, position: Int) {
                    tvUserClass.text = content
                }
            })
            mDialog?.show(supportFragmentManager, "hhh")

        }
        tvDeviceReason.setOnClickListener {
            mDialog?.setData(InitUtils.reasonList)
            mDialog?.setCallback(object : ChooseCallBack {
                override fun onChoose(content: String, position: Int) {
                    mReasonPosition = position
                    tvDeviceReason.text = content
                    if (mReasonPosition == 7) {
                        llOtherReason.visibility = View.VISIBLE
                        return
                    }

                    llOtherReason.visibility = View.GONE
                }
            })
            mDialog?.show(supportFragmentManager, "hhh")
        }
        tvTime.text = TimeUtils.getTime(System.currentTimeMillis(), "yyyy-MM-dd HH:mm")
        tvWeek.text = "KW${TimeUtils.getCurrentWeek()}"

    }

    override fun initData() {
    }

    fun checkInfo(): String {
        if (TextUtils.isEmpty(tvDeviceType.text)) {
            return "请选择设备型号"
        }
        if (mPosition != 5) {
            if (TextUtils.isEmpty(etDeviceNum1.text)) {
                return "请输入设备编号"
            }
        } else {
            if (TextUtils.isEmpty(etDeviceNum.text)) {
                return "请输入整机编号"
            }
        }
        if (TextUtils.isEmpty(tvUserLevel.text)) {
            return "请选择工段"
        }
        if (TextUtils.isEmpty(tvUserClass.text)) {
            return "请选择班组"
        }
        if (TextUtils.isEmpty(etUserName.text)) {
            return "请输入取件人姓名"
        }
        if (TextUtils.isEmpty(etUserPhone.text)) {
            return "请输入电话"
        }
        if (TextUtils.isEmpty(etUserNum.text)) {
            return "请输入工位号"
        }
        if (TextUtils.isEmpty(tvDeviceReason.text)) {
            return "请选择故障原因"
        }
        if (mReasonPosition == 7 && TextUtils.isEmpty(etOtherReason.text)) {
            return "请输入其他原因原因"
        }
        return ""
    }


    private fun getDeviceNo(): String {
        val sb = StringBuffer()
        if (mPosition < 5) {
            sb.append().append("-").append(etDeviceNum1.text).append("-").append(tvDeviceNum2.text)
        } else {
            sb.append(etDeviceNum.text)
        }
        return sb.toString()
    }

    private fun saveRecord() {
        recordVo = SmartRecordReq(
            "1",
            tvDeviceType.text.toString(),
            tvUserLevel.text.toString(),
            tvUserClass.text.toString(),
            etUserName.text.toString()
            ,
            etUserPhone.text.toString(),
            etUserNum.text.toString(),
            tvTime.text.toString(),
            tvWeek.text.toString(),
            tvDeviceReason.text.toString()
            ,
            InitUtils.deviceNameList[mPosition],
            getDeviceNo(),
            ""
        )
        UploadGoodsActivity.startAction(this@GiveBackActivity, recordVo!!, false, mPosition)

    }
}