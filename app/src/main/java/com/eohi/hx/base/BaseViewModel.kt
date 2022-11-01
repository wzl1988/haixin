package com.eohi.hx.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.HttpUtil
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.utils.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLEncoder
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


open class BaseViewModel : ViewModel() {

    private val AUTH_SECRET = "123456"//前后台协议密钥

    val httpUtil by lazy { HttpUtil.getInstance().getService() }

    var isShowLoading = MutableLiveData<Boolean>()//是否显示loading
    var errorData = MutableLiveData<ErrorResult>()//错误信息

    public fun showLoading() {
        isShowLoading.value = true
    }

    public fun dismissLoading() {
        isShowLoading.value = false
    }

    public fun showError(error: ErrorResult) {
        errorData.value = error
    }

    /**
     * 无参
     */
    open fun signNoParams(): LinkedHashMap<String, String?> {
        val params = LinkedHashMap<String, String?>()
        params["sign"] = getSign(params)
        return params
    }

    /**
     * 有参
     */
    open fun signParams(params: LinkedHashMap<String, String?>): LinkedHashMap<String, String?> {
        params["sign"] = getSign(params)
        return params
    }


    /**
     * 签名
     */
    private fun getSign(params: LinkedHashMap<String, String?>): String {
        val sb = StringBuilder()
        params.forEach {
            val key = it.key
            var value = ""
            if (!it.value.isNullOrEmpty()) {
                value = URLEncoder.encode(it.value).replace("\\+", "%20")
            }
            sb.append("$key=$value&")
        }
        val s = sb.toString().substring(0, sb.toString().length - 1).toLowerCase() + AUTH_SECRET
        return encryption(s)
    }


    /**
     * MD5加密
     *
     * @param plainText 明文
     * @return 32位密文
     */
    private fun encryption(plainText: String): String {
        var re_md5 = ""
        try {
            val md: MessageDigest = MessageDigest.getInstance("MD5")
            md.update(plainText.toByteArray())
            val b: ByteArray = md.digest()
            var i: Int
            val buf = StringBuffer("")
            for (offset in b.indices) {
                i = b[offset].toInt()
                if (i < 0) i += 256
                if (i < 16) buf.append("0")
                buf.append(Integer.toHexString(i))
            }
            re_md5 = buf.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return re_md5
    }


    /**
     * 请求接口，可定制是否显示loading和错误提示
     */
    fun <T> launch(
        block: suspend CoroutineScope.() -> BaseResModel<T>,//请求接口方法，T表示data实体泛型，调用时可将data对应的bean传入即可
        liveData: MutableLiveData<T>,
        isShowLoading: Boolean = false,
        isShowError: Boolean = true
    ) {
        if (isShowLoading) showLoading()
        viewModelScope.launch {
            try {
//                 var result: BaseResult<T>? = null
//                 withContext(Dispatchers.IO) {
//                     result = block()
//                 }
//                 if (result!!.errorCode == 0) {//请求成功
//                     liveData.value = result!!.data
//                 } else {
//                     LogUtil.e("请求错误>>" + result!!.errorMsg)
//                     showError(ErrorResult(result!!.errorCode, result!!.errorMsg, isShowError))
//                 }
                val result = withContext(Dispatchers.IO) { block() }
                if (result.code == 1) {//请求成功
                    liveData.value = result.data?.list!![0];
                } else {
                    LogUtil.e("请求错误>>" + result.msg)
                    showError(ErrorResult(result.code, result.msg, isShowError))
                }
            } catch (e: Throwable) {//接口请求失败
                LogUtil.e("请求异常>>" + e.message)
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = isShowError
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }
        }
    }


    //返回列表
    fun <T> launchList(
        block: suspend CoroutineScope.() -> BaseResModel<T>,//请求接口方法，T表示data实体泛型，调用时可将data对应的bean传入即可
        liveData: MutableLiveData<ArrayList<T>>,
        isShowLoading: Boolean = false,
        isShowError: Boolean = true,
        successCode: Int = 1000
    ) {
        if (isShowLoading) showLoading()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { block() }
                if (result.code == successCode) {//请求成功
                    liveData.value = result.data?.list;
                } else {
                    LogUtil.e("请求错误>>" + result.msg)
                    showError(ErrorResult(result.code, result.msg, isShowError))
                }
            } catch (e: Throwable) {//接口请求失败
                LogUtil.e("请求异常>>" + e.message)
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = isShowError
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }
        }
    }


}
