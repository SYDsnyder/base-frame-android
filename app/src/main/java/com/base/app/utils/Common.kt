package com.base.app.utils

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Checkable
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import com.base.app.R
import java.lang.reflect.ParameterizedType
import java.math.BigDecimal
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.math.roundToInt


//显示软键盘
fun showSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val inputMethodManager =
            act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.toggleSoftInput(
                InputMethodManager.SHOW_IMPLICIT,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

//隐藏软键盘
fun hideSoftKeyboard(activity: Activity?) {
    activity?.let { act ->
        val view = act.currentFocus
        view?.let {
            val inputMethodManager =
                act.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}

//初始化toolbar 不使用toolbar标题
fun Toolbar.initBar(onBack: (toolbar: Toolbar) -> Unit): Toolbar {
    setBackgroundColor(resources.getColor(R.color.c_d0121b))
    setNavigationIcon(R.mipmap.back_white)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}

//初始化普通的toolbar 只设置标题
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(resources.getColor(R.color.c_d0121b))
    title = titleStr
    return this
}

//隐藏底部虚拟按键
fun hideNavigationBar(activity: Activity) {
    val decorView: View = activity.window.decorView
    val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or decorView.systemUiVisibility)
    // or View.SYSTEM_UI_FLAG_FULLSCREEN 隐藏标题栏
    decorView.systemUiVisibility = uiOptions
}


var <T : View> T.lastClickTime: Long
    set(value) = setTag(1766613352, value)
    get() = getTag(1766613352) as? Long ?: 0

inline fun <T : View> T.singleClick(time: Long = 800, crossinline block: (T) -> Unit) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}
//兼容点击事件设置为this的情况
fun <T : View> T.singleClick(onClickListener: View.OnClickListener, time: Long = 1000) {
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime > time || this is Checkable) {
            lastClickTime = currentTimeMillis
            onClickListener.onClick(this)
        }
    }
}

//获取当前类绑定的泛型ViewModel-clazz
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

//格式化金额
fun formatBigDecimal(bigDecimal: BigDecimal): String {
    val num: Double = bigDecimal.toDouble()
    if (num.roundToInt() - num == 0.0) {
        return num.toInt().toString()
    }
    return num.toString()
}

//md5加密
@NonNull
fun md5(string: String): String {
    if (TextUtils.isEmpty(string)) {
        return ""
    }
    try {
        val md5: MessageDigest = MessageDigest.getInstance("MD5")
        val bytes: ByteArray = md5.digest(string.toByteArray())
        val result = java.lang.StringBuilder()
        for (b in bytes) {
            var temp = Integer.toHexString(b.toInt() and 0xff)
            if (temp.length == 1) {
                temp = "0$temp"
            }
            result.append(temp)
        }
        return result.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}