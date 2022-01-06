package com.base.app.network

import com.base.app.base.BaseResult
import com.base.app.bean.*
import retrofit2.http.*


interface ApiService {

    //账号密码登录
    @POST("api/app/user/login")
    @FormUrlEncoded
    suspend fun userLogin(
        @Field("account") account: String,
        @Field("password") pwd: String,
        @Field("appType") appType: String = "android"
    ): BaseResult<String>

    //获取用户信息
    @GET("api/app/user/detail")
    suspend fun getUserInfo(@Query("id") id: String): BaseResult<UserInfoBean>

}