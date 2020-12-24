package com.car.bolang.bean

import java.io.Serializable

data class ProblemListVO(
    val code: Int,
    val `data`: ProblemData,
    val message: String
)

data class ProblemData(
    val current: Int,
    val hitCount: Boolean,
    val orders: List<Any>,
    val pages: Int,
    val records: List<ProblemRecord>,
    val searchCount: Boolean,
    val size: Int,
    val total: Int
)

data class ProblemRecord(
    val answer: String,
    val answerDate: String,
    val answerId: Int,
    val createTime: String,
    val createUser: Int,
    val describes: String,
    val id: Int,
    val img: String,
    val isDelete: Boolean,
    val quizId: Int,
    val quizTime: String,
    val updateTime: String,
    val updateUser: Int
):Serializable