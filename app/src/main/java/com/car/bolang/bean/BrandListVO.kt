package com.car.bolang.bean

data class BrandListVO(
    val code: Int,
    val `data`: BrandData,
    val message: String
)

data class BrandData(
    val current: Int,
    val hitCount: Boolean,
    val orders: List<Any>,
    val pages: Int,
    val records: List<BrandRecord>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)

data class BrandRecord(
    val createTime: Any,
    val createUser: Any,
    val firstletter: String,
    val id: Int,
    val img: String,
    val isDelete: Boolean,
    val name: String,
    val spell: String,
    val updateTime: Any,
    val updateUser: Any,
    val price:Double
)