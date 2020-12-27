package com.car.bolang.bean

import java.io.Serializable

data class GoodReq (var name:String ,var brandId:String, var pageNum:Int ,var pageSize:Int)


data class AskQuestionsReq(var describes:String ,var img:String, var quizId:String)

data class PaymentReq(var totalFee:Int,var attach:String)


data class  SmartRecordReq(var isGet :String ,var deviceType:String ,var groupLevel:String ,var classLevel:String ,var userName:String ,
                           var phone:String ,var userPosition  :String ,var date:String ,var week:String ,var reason:String
                           ,var deviceName:String,var deviceNo:String,var devicePicture:String):
    Serializable