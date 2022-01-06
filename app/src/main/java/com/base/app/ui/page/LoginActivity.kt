package com.base.app.ui.page

import android.os.Bundle
import com.base.app.R
import com.base.app.base.BaseActivity
import com.base.app.ui.viewmodel.LoginViewModel


class LoginActivity : BaseActivity<LoginViewModel>() {


    override fun layoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView(savedInstanceState: Bundle?) {

    }
}
