package com.eohi.hx.base

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.utils.LogUtil
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {
    lateinit var mContext: FragmentActivity
    lateinit var vm: VM
    lateinit var v: VB

    private var loadingDialog: ProgressDialog? = null

    companion object {
        var accout by Preference("userid", "")
        var username by Preference("username", "")
        var companyNo: String by Preference("companyNo", "")
        var companyname by Preference("companyname", "")
        var apiurl by Preference("ApiUrl", "http://192.168.0.26:3019/")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initResources()

        //注意 type.actualTypeArguments[0]=BaseViewModel，type.actualTypeArguments[1]=ViewBinding
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz1 = type.actualTypeArguments[0] as Class<VM>
        vm = ViewModelProvider(this).get(clazz1)

        val clazz2 = type.actualTypeArguments[1] as Class<VB>
        val method = clazz2.getMethod("inflate", LayoutInflater::class.java)
        v = method.invoke(null, layoutInflater) as VB

        setContentView(v.root)

        mContext = this
        init()
        initData()
        initView()
        initClick()
        initVM()
        LogUtil.e(getClassName())
    }

    /**
     * 防止系统字体影响到app的字体
     *
     * @return
     */
    open fun initResources(): Resources? {
        val res: Resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isNeedEventBus())
            EventBus.getDefault().unregister(this)
    }

    abstract fun isNeedEventBus(): Boolean

    //事件传递
    @Subscribe
    fun onEventMainThread(msg: EventMessage) {
        handleEvent(msg)
    }

    open fun getClassName(): String? {
        val className = "BaseActivity"
        try {
            return javaClass.name
        } catch (e: Exception) {
        }
        return className
    }

    abstract fun initView()

    abstract fun initClick()

    abstract fun initData()

    abstract fun initVM()

    private fun init() {
        if (isNeedEventBus())
            EventBus.getDefault().register(this)
        //loading
        vm.isShowLoading.observe(this) {
            if (it) showLoading() else dismissLoading()
        }
        //错误信息
        vm.errorData.observe(this) {
            if (it.show) ToastUtil.showToast(mContext, it.errMsg)
            errorResult(it)
        }
    }

    private fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this)
        }
        loadingDialog!!.show()
    }

    private fun dismissLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     * 消息、事件接收回调
     */
    open fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.LOGIN_OUT) {
            finish()
        }
    }

    /**
     * 接口请求错误回调
     */
    open fun errorResult(errorResult: ErrorResult) {}

    /**
     * 解决Fragment中的onActivityResult()方法无响应问题。
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments=supportFragmentManager.fragments
        fragments?.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

}