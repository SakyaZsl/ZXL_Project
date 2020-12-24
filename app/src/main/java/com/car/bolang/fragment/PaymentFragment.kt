package com.car.bolang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.car.bolang.R
import com.car.bolang.inter.PaymentCallback
import kotlinx.android.synthetic.main.fragment_payment.*

class PaymentFragment :DialogFragment(){

    private var payNumber:Float=0f
    private var callback: PaymentCallback?=null


    fun  setPayCallback( callback: PaymentCallback){
        this.callback=callback
    }


    fun setPayNumber(number:Float){
        this.payNumber=number
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFullScreen)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }


    private fun initView(){
        btnPayment.text="确认支付${payNumber}元"
        btnPayment.setOnClickListener {
            dismiss()
            callback?.let {
                it.startPayment(payNumber)
            }
        }
    }
}