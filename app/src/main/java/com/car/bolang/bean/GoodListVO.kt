package com.car.bolang.bean

import java.io.Serializable

data class GoodListVO(
    val code: Int,
    val `data`: Data,
    val message: String
)

data class Data(
    val current: Int,
    val hitCount: Boolean,
    val orders: List<Any>,
    val pages: Int,
    val records: List<Record>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)

data class Record(
    val brandId: Int,
    val brandName: String,
    val createTime: String,
    val depreciationRate: Double,
    val describes: String,
    val id: Int,
    val img: String,
    val isDelete: Boolean,
    val name: String,
    val palladium: Double,
    val platinum: Double,
    val price: Double,
    val rhodium: Double,
    val subTitle: String,
    val title: String,
    val updateTime: String,
    val homeImg: String,
    val weight: Double
):Serializable


data class GoodsVO(val img: String,val name: String)