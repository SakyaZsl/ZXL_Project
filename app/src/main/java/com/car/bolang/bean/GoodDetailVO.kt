package com.car.bolang.bean

data class GoodDetailVO(
    val code: Int,
    val `data`: GoodData,
    val message: String
)

data class GoodData(
    val brandId: Int,
    val createTime: String,
    val createUser: Int,
    val depreciationRate: Double,
    val describes: String,
    val id: Int,
    val img: String,
    val homeImg: String,
    val rotationChart: String,
    val isDelete: Boolean,
    val name: String,
    val palladium: Double,
    val platinum: Double,
    val price: Double,
    val rhodium: Double,
    val subTitle: String,
    val title: String,
    val updateTime: String,
    val updateUser: Int,
    val weight: Double
)