package com.base.app.utils

import android.R
import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import kotlin.math.roundToLong

object DisplayUtil {
    fun displayWidth(mContext: Context): Int {
        val dm2 = mContext.resources.displayMetrics
        return dm2.widthPixels
    }

    fun displayHeight(mContext: Context): Int {
        val dm2 = mContext.resources.displayMetrics
        return dm2.heightPixels
    }

    fun dp2px(mContext: Context?, dpValue: Float): Int {
        if (mContext == null || compareFloat(0f, dpValue) == 0) return 0
        val scale = mContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun dp2px(mContext: Context?, dpValue: Int): Int {
        if (mContext == null || compareFloat(0f, dpValue.toFloat()) == 0) return 0
        val scale = mContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun px2dp(mContext: Context, px: Int): Int {
        val displayMetrics = mContext.resources.displayMetrics
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }

    fun getActionBarHeight(mContext: Context): Int {
        val tv = TypedValue()
        return if (mContext.theme
                .resolveAttribute(R.attr.actionBarSize, tv, true)
        ) {
            TypedValue.complexToDimensionPixelSize(
                tv.data,
                mContext.resources.displayMetrics
            )
        } else {
            0
        }
    }

    fun getStatusHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getPhoneHeightPixels(mContext: Context): Int {
        val var2 = DisplayMetrics()
        (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            var2
        )
        return var2.heightPixels
    }

    fun getPhoneWidthPixels(mContext: Context): Int {
        val var2 = DisplayMetrics()
        (mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            var2
        )
        return var2.widthPixels
    }

    private fun compareFloat(a: Float, b: Float): Int {
        val ta = (a * 1000000).roundToLong()
        val tb = (b * 1000000).roundToLong()
        return when {
            ta > tb -> {
                1
            }
            ta < tb -> {
                -1
            }
            else -> {
                0
            }
        }
    }
}