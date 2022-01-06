package com.base.app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.base.app.bean.ErrorResultBean
import com.base.app.network.RetrofitManager
import com.base.app.utils.ExceptionUtil
import kotlinx.coroutines.*

/**
 * Created by snyder.
 */
open class BaseViewModel : ViewModel() {

    val mService by lazy { RetrofitManager.getInstance().create() }
    var loadingStatus = MutableLiveData<Boolean>()//是否显示loading
    var errorData = MutableLiveData<ErrorResultBean>()//错误信息

    /**
     * 注意此方法传入的参数：api是以函数作为参数传入
     * api：即接口调用方法
     * isShowLoading: 是否展示loading
     * error：可以理解为接口请求失败回调
     * ->数据类型，表示方法返回该数据类型
     */
    fun <T> launch(
        api: suspend CoroutineScope.() -> BaseResult<T>,//请求接口方法，T表示data实体泛型，调用时可将data对应的bean传入即可
        liveData: MutableLiveData<T>,
        isShowLoading: Boolean = true
    ) {
        if (isShowLoading) loadingStatus.value = true
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {//异步请求接口
                    val result = api()
                    withContext(Dispatchers.Main) {
                        loadingStatus.value = false
                        if (result.code == 100) {//请求成功
                            liveData.value = result.data
                        } else {
                            errorData.value = ErrorResultBean(result.code, result.msg)
                        }
                    }
                }
            } catch (e: Throwable) {
                loadingStatus.value = false
                //接口请求失败
                errorData.value = ErrorResultBean(-1, ExceptionUtil.catchException(e))
            }
        }
    }
}