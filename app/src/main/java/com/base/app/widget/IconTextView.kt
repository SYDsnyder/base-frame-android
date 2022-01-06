package com.base.app.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.base.app.R
import kotlinx.android.synthetic.main.icon_text_view.view.*


class IconTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int):this(context,attrs,defStyleAttr,0)
    constructor(context: Context, attrs: AttributeSet?):this(context,attrs,0)
    constructor(context: Context):this(context,null)

    //android:foreground="?android:attr/selectableItemBackground"
    init {
        LayoutInflater.from(context).inflate(R.layout.icon_text_view,this,true)
        orientation= HORIZONTAL
        gravity=Gravity.CENTER_VERTICAL
        tv_txt.paint.isFakeBoldText = true

        //内边距
        setPadding(context.resources.getDimensionPixelSize(R.dimen.dp_16))
        setBackgroundResource(R.color.white)
        val ta=context.obtainStyledAttributes(attrs,R.styleable.IconTvView)
        try{
            ta.getDrawable(R.styleable.IconTvView_dlx_icon)?.apply {
                iv_icon.setImageDrawable(this)
            }
            ta.getString(R.styleable.IconTvView_dlx_txt)?.apply {
                tv_txt.text=this
            }
        }catch (t:Throwable){
            t.printStackTrace()
        }
        setOnClickListener {
            iconTvListener?.clicked()
        }
        ta.recycle()
    }

    var iconTvListener:IconTvListener?=null
    interface IconTvListener{
        fun clicked()
    }

}