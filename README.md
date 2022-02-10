# base-frame-android
基于kotlin协程实现MVVM模式的基本Android框架，对retrofit进行二次封装，一行代码实现网络请求

// 账号密码登录
fun userLogin(account: String, password: String) {
    launch({ mService.userLogin(account, password) }, loginData)
}
登录请求搞定，就是这么简单...
