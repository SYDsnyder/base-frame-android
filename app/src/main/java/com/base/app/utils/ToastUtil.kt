package com.base.app.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast
import com.base.app.App


object ToastUtil {

    private lateinit var mToast: Toast
    private var yOffset = 0// 弹出toast默认的 距底部的距离


    private fun init(context: Context?) {
        requireNotNull(context) { "Context should not be null" }
        if (mToast == null) {
            mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
            yOffset = mToast.yOffset
        }
    }

    //这个方法  选择性使用
    fun show(resId: Int) {
        show(App.instance.resources.getString(resId))
    }

    //常用方法
    fun show(content: String?) {
        show(content, Gravity.BOTTOM)
    }

    fun show(content: String?, gravity: Int) {
        show(content, gravity, Toast.LENGTH_SHORT)
    }

    fun show(content: String?, gravity: Int, duration: Int) {
        if(mToast == null) init(App.instance)
        mToast.setText(content)
        mToast.duration = duration
        mToast.setGravity(gravity, 0, yOffset)
        mToast.show()
    }
}