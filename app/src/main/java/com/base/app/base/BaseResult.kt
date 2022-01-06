package com.base.app.base

/**
 * Created by snyder.
 */
data class BaseResult<out T>(val code:Int?, val msg:String?,
                             val data:T, val success:Boolean)