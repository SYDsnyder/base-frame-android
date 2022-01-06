package com.base.app.network

import com.base.app.common.Constant
import com.base.app.utils.CacheUtil
import com.base.app.utils.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by snyder.
 */
object RetrofitManager {

    @Volatile
    private var retrofitManager: RetrofitManager? = null
    private var retrofit: Retrofit

    fun getInstance() = retrofitManager ?: synchronized(this) {
        retrofitManager ?: RetrofitManager.also { retrofitManager = it }
    }

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(Constant.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(genericOkClient())
            .build()
    }

    fun create(): ApiService = retrofit.create(ApiService::class.java)

    private fun genericOkClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor { message -> Logger.jsonPrint(message) }

        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val headerInterceptor = Interceptor { chain ->
            val request: Request = chain.request()
            val builder: Request.Builder = request.newBuilder()
            builder.method(request.method(), request.body())
//            builder.addHeader("Content-Type", "application/json")
            builder.addHeader("token", CacheUtil.getToken())
            val build: Request = builder.build()
            chain.proceed(build)
        }
        val sc: SSLContext = SSLContext.getInstance("SSL")
        sc.init(null, arrayOf<TrustManager>(object : X509TrustManager {

            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf<X509Certificate>()
            }
        }), SecureRandom())

        val builder = OkHttpClient.Builder()
            .connectTimeout(10_000L, TimeUnit.MILLISECONDS)
            .readTimeout(30_000, TimeUnit.MILLISECONDS)
            .writeTimeout(30_000, TimeUnit.MILLISECONDS)
            .addInterceptor(headerInterceptor)
            .addInterceptor(httpLoggingInterceptor)

        builder.sslSocketFactory(sc.socketFactory)
            .hostnameVerifier { p0, p1 -> true }

        return builder.build()
    }
}