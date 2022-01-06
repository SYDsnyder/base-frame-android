package com.base.app.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import com.base.app.base.BaseViewModel
import com.base.app.bean.UserInfoBean

class LoginViewModel : BaseViewModel() {
    val loginData = MutableLiveData<String>()
    val userInfoData = MutableLiveData<UserInfoBean>()

    // 账号密码登录
    fun userLogin(account: String, password: String) {
        launch({ mService.userLogin(account, password) }, loginData)
    }

    // 获取用户信息
    fun getUserInfo(id: String) {
        launch({ mService.getUserInfo(id) }, userInfoData)
    }
}