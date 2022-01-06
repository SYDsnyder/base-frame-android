package com.base.app.bean


data class UserInfoBean(
    var id: String,
    var account: String,
    var name: String,
    var avatar: String,
    var roleId: String,
    var root: Boolean,
    var accountLevel: String
)

