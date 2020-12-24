package com.car.bolang.bean

import java.io.Serializable

data class SmartModelVo(
    val createTime: String,
    val createUser: Int,
    val describes: String
): Serializable