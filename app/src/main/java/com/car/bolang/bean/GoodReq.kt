package com.car.bolang.bean

data class GoodReq (var name:String ,var brandId:String, var pageNum:Int ,var pageSize:Int)


data class AskQuestionsReq(var describes:String ,var img:String, var quizId:String)

data class PaymentReq(var totalFee:Int,var attach:String)