package com.car.bolang.bean

data class QuestionOrderVO(
    val code: Int,
    val `data`: QuestionData,
    val message: String
)

data class QuestionData(
    val amount: Float,
    val createTime: String,
    val createUser: Int,
    val id: Int,
    val isDelete: Boolean,
    val name: String,
    val sort: Int,
    val states: Boolean,
    val type: Int,
    val updateTime: Any,
    val updateUser: Int
)