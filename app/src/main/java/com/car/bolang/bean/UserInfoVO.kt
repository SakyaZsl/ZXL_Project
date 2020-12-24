package com.car.bolang.bean

data class UserInfoVO(
    val code: Int,
    val `data`: UserData,
    val message: String
)

data class UserData(
    val createTime: String,
    val createUser: Any,
    val grade: Any,
    val id: Int,
    val img: Any,
    val invitationCode: String,
    val inviteesCode: Any,
    val isDelete: Boolean,
    val memberExpireTime: String,
    val password: Any,
    val states: Int,
    val token: String,
    val updateTime: String,
    val updateUser: Any,
    val username: String
)