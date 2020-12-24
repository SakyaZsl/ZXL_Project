package com.car.bolang.bean

data class PaymentResultVO(
    val code: Int,
    val `data`: PaymentData,
    val message: String
)

data class PaymentData(
    val appId: String,
    val nonceStr: String,
    val `package`: String,
    val partnerid: String,
    val paySign: String,
    val prepayid: String,
    val signType: String,
    val timeStamp: String
)