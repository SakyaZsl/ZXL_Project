package com.car.bolang.bean

data class MemberListVO(
    val code: Int,
    val `data`: MemberData,
    val message: String
)

data class MemberData(
    val current: Int,
    val hitCount: Boolean,
    val orders: List<Any>,
    val pages: Int,
    val records: List<MemberRecord>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)

data class MemberRecord(
    val amount: Int,
    val createTime: String,
    val createUser: Int,
    val id: Int,
    val isDelete: Boolean,
    val name: String,
    val sort: Int,
    val states: Boolean,
    val updateTime: String,
    val updateUser: Int
)