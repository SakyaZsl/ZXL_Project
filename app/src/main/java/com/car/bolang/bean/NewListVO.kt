package com.car.bolang.bean

data class NewListVO(
    val code: Int,
    val `data`: List<NewData>,
    val message: String
)

data class NewData(
    val createTime: String,
    val firstPicture: String,
    val path: String,
    val title: String
)