package com.car.bolang.bean

import java.io.Serializable

data class HomePageVO(
    val code: Int,
    val `data`: HomeData,
    val message: String
)

data class HomeData(
    val goldPriceList: List<GoldPrice>,
    val goodsList: List<Goods>,
    val reaTime:String,
    val isExpire:Boolean=false
)

data class GoldPrice(
    val name: String,
    val price: String,
    val upAndDown: String
)

data class Goods(
    val id: Int,
    val img: String,
    val name: String
    ):Serializable