package com.car.bolang.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.car.bolang.R
import com.car.bolang.inter.CancelAndSureCallback
import kotlinx.android.synthetic.main.dialog_cancel_and_sure.*

class CancelAndSureDialog(context: Context) :Dialog(context) {
    private var mTitleText=""
    private var mContentText=""
    private var mCallback: CancelAndSureCallback ?=null

    fun  setData(title:String,content:String){
        mTitleText=title
        mContentText=content
    }

    fun  setCallback(callback:CancelAndSureCallback){
        this.mCallback=callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_cancel_and_sure)
        initView()
    }




    private fun initView(){
        if(!TextUtils.isEmpty(mTitleText)){
            tvTitle.text=mTitleText
        }
        if(!TextUtils.isEmpty(mContentText)){
            tvContent.text=mContentText
        }
        tvCancel.setOnClickListener {
            dismiss()
            mCallback?.let {
                it.onCancel()
            }
        }
        tvSure.setOnClickListener {
            dismiss()
            mCallback?.let {
                it.onSure()
            }
        }
    }
}