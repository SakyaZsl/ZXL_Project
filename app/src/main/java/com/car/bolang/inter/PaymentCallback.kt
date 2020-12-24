package com.car.bolang.inter

interface PaymentCallback {
    fun startPayment(pay: Float)
}

interface TakePictureCallback {
    fun takeCamera()
    fun takeAlbum()
}

interface CancelAndSureCallback {
    fun onSure()
    fun onCancel()
}


interface ShareAppCallback{
    fun  onAppShare()
}

interface ChooseCallBack{
    fun onChoose(content:String,position:Int)
}