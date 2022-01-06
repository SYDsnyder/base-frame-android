package com.base.app.widget

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.CountDownTimer
import android.widget.TextView

/**
 * Created by snyder.
 * 获取验证码倒计时
 */
class CountDownTimerView(
    private val mTextView: TextView,
    millisInFuture: Long,
    countDownInterval: Long
) : CountDownTimer(millisInFuture, countDownInterval) {

    @SuppressLint("ResourceAsColor")
    override fun onTick(millisUntilFinished: Long) {
        mTextView.isClickable = false //设置不可点击
        mTextView.text = "重新发送(${(millisUntilFinished / 1000)})" //设置倒计时时间
        mTextView.setTextColor(Color.GRAY) //设置按钮为灰色，这时是不能点击的
    }

    override fun onFinish() {
        mTextView.text = "重新发送"
        mTextView.isClickable = true //重新获得点击
        mTextView.setTextColor(Color.parseColor("#D0121B"))//还原背景色
    }
}