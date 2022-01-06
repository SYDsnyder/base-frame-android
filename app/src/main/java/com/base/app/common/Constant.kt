package com.base.app.common

object Constant {
    var Environment: String = ""

    //环境域名
    fun getUrl(): String {
        return when (Environment) {
            "dev" -> ""
            "test" -> ""
            "pre" -> ""
            else -> ""
        }
    }
}