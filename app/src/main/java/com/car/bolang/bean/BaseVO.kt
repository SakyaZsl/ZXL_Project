package com.car.bolang.bean

import java.io.File

const val SUCCESS_CODE="00"
class BaseVO(var code:String="",var msg:String="")



data class UploadImageVO(
    val code: String,
    val files: List<FileInfo>,
    val msg: String
)

data class FileInfo(
    val deleteType: String,
    val deleteUrl: String,
    val fileid: String,
    val name: String,
    val size: Int,
    val suffix: String,
    val url: String
)