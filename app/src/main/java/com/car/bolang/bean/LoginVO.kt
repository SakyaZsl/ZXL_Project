package com.car.bolang.bean

data class LoginVO(
    var code: Int, var message: String,
    var data: LoginData
)

data class LoginData(var token: String, var user: LoginUser)


data class LoginUser(
    var id: String, var isDelete: Boolean, var username: String
    , var password: String, var img: String, var invitationCode: String,
    var inviteesCode: String, var grade: String,var memberExpireTime:String
)


data class UploadImgVO(var code: Int, var message: String,
                       var data: String)


data class UpLoadDeviceType(var isGet: String,var deviceType: String,var groupLevel: String,var classLevel: String,var userName: String,
                            var phone: String,var userPosition: String,var date: String,var week: String,var reason: String)


//{"isGet":"1","deviceType":"手机","
// groupLevel":"3组","classLevel":"5班","userName":"张晓亮","phone":"13788888888","userPosition":"3工",
// "date":"2020-12-14 17:07:43","week":"48","reason":"暂无"}