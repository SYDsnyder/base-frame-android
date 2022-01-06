package com.base.app.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by snyder.
 * RecyclerView添加分割线
 */
class LineItemDecoration(
    context: Context,
    @ColorRes private val colorRes: Int,
    @DimenRes private val dpRes: Int,
    private val isLastItemDrawLine: Boolean = true,
    private val haveFooter: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dpValue: Float

    init {
        mPaint.color = ContextCompat.getColor(context, colorRes)
        dpValue = context.resources.getDimension(dpRes)
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = dpValue.toInt()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        if (isLastItemDrawLine) {
            for (i in parent.children) {
                val top = i.bottom
                val bottom = i.bottom + dpValue
                c.drawRect(RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom), mPaint)
            }
        } else {
            if (haveFooter) {
                for (i in 0 until parent.childCount - 2) {
                    val top = parent.getChildAt(i).bottom
                    val bottom = parent.getChildAt(i).bottom + dpValue
                    c.drawRect(RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom), mPaint)
                }
            }else {
                for (i in 0 until parent.childCount - 1) {
                    val top = parent.getChildAt(i).bottom
                    val bottom = parent.getChildAt(i).bottom + dpValue
                    c.drawRect(RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom), mPaint)
                }
            }
        }
    }
}