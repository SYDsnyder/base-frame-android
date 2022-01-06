package com.base.app

import android.app.Application
import com.base.app.common.Constant
import com.base.app.utils.CacheUtil
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV


class App : Application() {

    companion object {
        lateinit var instance: App

        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                MaterialHeader(context)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        MMKV.initialize(this)
        initEnvironment()
    }

    private fun initEnvironment() {
        if (!BuildConfig.DEBUG) {
            Constant.Environment = BuildConfig.ENVIRONMENT
        } else {
            val environmentCache = CacheUtil.getEnvironment()
            if (environmentCache.isEmpty()) {
                Constant.Environment = BuildConfig.ENVIRONMENT
            } else {
                Constant.Environment = environmentCache
            }
        }
    }
}

