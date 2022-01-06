package com.base.app.utils

import android.util.Log
import com.base.app.BuildConfig
import org.json.JSONArray
import org.json.JSONObject

object Logger {
    private const val TAG = "Logger"
    private var enabled = BuildConfig.DEBUG
    fun e(msg: String?) {
        if (!enabled) return
        Log.e(TAG, msg)
    }

    fun e(tag: String, msg: String?) {
        if (!enabled) return
        Log.e(tag, msg)
    }

    fun i(msg: String?) {
        if (!enabled) return
        Log.i(TAG, msg)
    }

    fun i(tag: String, msg: String?) {
        if (!enabled) return
        Log.i(tag, msg)
    }

    fun jsonPrint(msg: Any?) {
        if (!enabled) return
        val json = msg.toString()

        val jsonInformation = try {
            when {
                json.startsWith("{") && json.endsWith("}") -> JSONObject(json).toString(4)
                json.startsWith("[") && json.endsWith("]") -> JSONArray(json).toString(4)
                else -> "bad json information: ($json)"
            }
        } catch (e: Exception) {
            "${e.cause?.message}${System.getProperty("line.separator")}: $json"
        }
        var index = 0
        val segmentSize = 3 * 1024
        while (index < jsonInformation.length) {
            if (jsonInformation.length < index + segmentSize) {
                e("NetWork", jsonInformation.substring(index))
            } else {
                e("NetWork", jsonInformation.substring(index,index+segmentSize))
            }
            index += segmentSize
        }
    }
}