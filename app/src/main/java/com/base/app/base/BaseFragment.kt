package com.base.app.base

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.base.app.R
import com.base.app.bean.ErrorResultBean
import com.base.app.utils.Logger
import com.base.app.utils.getVmClazz

/**
 * Created by snyder.
 */
abstract class BaseFragment<VM : BaseViewModel> : Fragment() {

    private var alertDialog: AlertDialog? = null
    lateinit var mViewModel: VM
    private var toast: Toast? = null

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId(), container, false)
    }

    abstract fun initView(rootView: View)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Logger.e("Enter fragment", this.javaClass.name)
        mViewModel = createViewModel()
        initView(view)

        mViewModel.loadingStatus.observe(requireActivity(), Observer {
            if (it) showLoadingDialog() else dismissLoadingDialog()
        })

        mViewModel.errorData.observe(requireActivity(), Observer {
            errorData(it)
            it.errorMsg?.apply {
                showToast(this)
            }
        })
    }

    //请求失败后可重写该方法执行其他操作，基类只做了提示操作
    open fun errorData(it: ErrorResultBean?) {
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }

    fun showToast(msg: String?) {
        if (msg != null) {
            if (null == toast) {
                toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            } else {
                toast!!.setText(msg)
            }
            toast?.show()
        }
    }

    @SuppressLint("ShowToast")
    fun showToast(msg: Int) {
        if (null == toast) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        } else {
            toast!!.setText(msg)
        }
        toast?.show()
    }

    open fun showLoadingDialog() {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(activity, R.style.CustomProgressDialog).create()
        }
        alertDialog?.apply {
            window!!.setBackgroundDrawable(ColorDrawable())
            setOnKeyListener { dialog, keyCode, event -> keyCode == KeyEvent.KEYCODE_SEARCH || keyCode == KeyEvent.KEYCODE_BACK }
            val loadView: View = LayoutInflater.from(activity).inflate(R.layout.loading_alert, null)
            setView(loadView)
            setCanceledOnTouchOutside(false)
            show()
//        setContentView(R.layout.loading_alert)
        }
    }

    open fun dismissLoadingDialog() {
        alertDialog?.apply {
            if (isShowing) {
                dismiss()
            }
        }
    }
}