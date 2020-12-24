package com.car.bolang.bean

data class BaseResponse<T>(
    var code: Int = 0,
    var message: String = "",
    var data: T? = null
)

