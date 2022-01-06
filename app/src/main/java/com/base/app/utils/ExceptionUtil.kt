package com.base.app.utils

import android.accounts.NetworkErrorException
import android.util.MalformedJsonException
import androidx.annotation.StringRes
import com.base.app.App
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by snyder.
 */
object ExceptionUtil {

    /**
     * 处理异常
     */
    fun catchException(e: Throwable) : String{
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                return "网络异常"
            }
            is SocketTimeoutException -> {
                return "网络连接超时"
            }
            is UnknownHostException, is NetworkErrorException -> {
                return "请检查网络"
            }
            is MalformedJsonException, is JsonSyntaxException -> {
                return "数据解析异常，请稍后重试"
            }
            is InterruptedIOException -> {
                return "服务器连接失败，请稍后重试"
            }
            is ConnectException -> {
                return "连接服务器失败"
            }
            else -> {
                return e.message?:""
            }
        }
    }

    /**
     * 处理异常，toast提示错误信息
     */
    fun toastException(e: Throwable) {
        e.printStackTrace()
        when (e) {
            is HttpException -> {
                showToast("网络异常")
            }
            is SocketTimeoutException -> {
                showToast("网络连接超时")
            }
            is UnknownHostException, is NetworkErrorException -> {
                showToast("请检查网络")
            }
            is MalformedJsonException, is JsonSyntaxException -> {
                showToast("数据解析异常，请稍后重试")
            }
            is InterruptedIOException -> {
                showToast("服务器连接失败，请稍后重试")
            }
            is ConnectException -> {
                showToast( "连接服务器失败")
            }
            else -> {
                showToast(e.message?:"")
            }
        }
    }

    /**
     * toast提示
     */
    private fun showToast(@StringRes errorMsg: Int, errorCode: Int = -1) {
        showToast(App.instance.getString(errorMsg), errorCode)
    }

    /**
     * toast提示
     */
    private fun showToast(errorMsg: String, errorCode: Int = -1) {
        if (errorCode == -1) {
            ToastUtil.show(errorMsg)
        } else {
            ToastUtil.show("$errorCode：$errorMsg")
        }
    }
}