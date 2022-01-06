package com.base.app.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.app.R
import com.base.app.bean.ErrorResultBean
import com.base.app.utils.AppManager
import com.base.app.utils.getVmClazz
import com.base.app.utils.initBar
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * Created by snyder.
 */
abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {

    private var toast: Toast? = null
    lateinit var mViewModel: VM
    private var alertDialog: AlertDialog? = null

    abstract fun layoutId(): Int

    abstract fun initView(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        AppManager.getInstance().addActivity(this)
        super.onCreate(savedInstanceState)
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(layoutId())

        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        initView(savedInstanceState)
        if (toolbar != null) {
            toolbar.initBar {
                onBackPressed()
            }
        }

        mViewModel.loadingStatus.observe(this, Observer {
            if (it) showLoadingDialog() else dismissLoadingDialog()
        })

        mViewModel.errorData.observe(this, Observer {
            errorData(it)
            it.errorMsg?.apply {
                showToast(this)
            }
        })
    }

    //请求失败后可重写该方法执行其他操作，基类只做了提示操作
    open fun errorData(it: ErrorResultBean?) {
    }

    @SuppressLint("ShowToast")
    fun showToast(msg: String) {
        if (null == toast) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast?.show()
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    /**
     * 点击edittext控件之外隐藏软键盘
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    open fun showLoadingDialog() {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(this, R.style.CustomProgressDialog).create()
        }
        alertDialog?.apply {
            window!!.setBackgroundDrawable(ColorDrawable())
            setOnKeyListener { dialog, keyCode, event -> keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK }
            val loadView: View =
                LayoutInflater.from(this@BaseActivity).inflate(R.layout.loading_alert, null)
            setView(loadView)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    open fun dismissLoadingDialog() {
        alertDialog?.apply {
            if (isShowing) {
                dismiss()
            }
        }
    }

    override fun onDestroy() {
        AppManager.getInstance().removeActivity(this)
        super.onDestroy()
    }

//    //禁用物理返回键
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return false;
//    }
}