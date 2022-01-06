package com.base.app.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.base.app.bean.*

object CacheUtil {

    fun getUserInfo(): UserInfoBean? {
        val kv = MMKV.mmkvWithID("base")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, UserInfoBean::class.java)
        }
    }

    fun setUserInfo(userInfo: UserInfoBean?) {
        val kv = MMKV.mmkvWithID("base")
        if (userInfo == null) {
            kv.encode("user", "")
        } else {
            kv.encode("user", Gson().toJson(userInfo))
        }
    }

    /**
     * 获取token
     */
    fun getToken(): String {
        val kv = MMKV.mmkvWithID("base")
        return kv.decodeString("token", "")
    }

    fun setToken(token: String) {
        Logger.e("token $token")
        val kv = MMKV.mmkvWithID("base")
        kv.encode("token", token)
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID("base")
        return kv.decodeBool("first", true)
    }

    fun setFirst(first: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("base")
        return kv.encode("first", first)
    }


    /**
     * 设置环境
     */
    fun setEnvironment(text: String?) {
        val kv = MMKV.mmkvWithID("base")
        if (text == null) {
            kv.encode("environment", "")
        } else {
            kv.encode("environment", text)
        }
    }

    fun getEnvironment(): String {
        val kv = MMKV.mmkvWithID("base")
        return kv.decodeString("environment", "")
    }
}