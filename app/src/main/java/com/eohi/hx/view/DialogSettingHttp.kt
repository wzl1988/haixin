package com.eohi.hx.view

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import com.eohi.hx.R
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.dialog_setting_http.*
import java.util.regex.Pattern

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/26 14:03
 */
class DialogSettingHttp(context: Context, themeResId: Int) : Dialog(context, themeResId) {
    private val PROXY_HTTP_MATCH =
        "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?" //http正则表达式
    private var apiurl by Preference<String>("ApiUrl", "http://10.2.40.10:8039/")

    constructor(context: Context):this(context, R.style.MyDialog){
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_setting_http)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        init()
    }

    private fun init() {
        et_url.setText(apiurl)
        val list = arrayOf("http://172.18.0.161:3019/","http://10.2.40.10:8039/")
        btncancle.clicks { dismiss() }
        buttonok.clicks {
            if(hasProxy(et_url.text.toString())){
                apiurl = et_url.text.toString()
            }else{
                ToastUtil.showToast(context,"API服务器地址格式错误")
            }
            dismiss()
        }
        iv_login.clicks {
            showListPopulWindow(et_url,list)
        }
    }

    private fun showListPopulWindow(mEditText: EditText, list: Array<String>) {
        val listPopupWindow = ListPopupWindow(context)
        listPopupWindow.setAdapter(
            ArrayAdapter(
                context,
                R.layout.pop_item,
                list
            )
        ) //用android内置布局，或设计自己的样式
        listPopupWindow.anchorView = mEditText //以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    context, R.color.white
                )
            )
        )
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //设置项点击监听
            mEditText.setText(list[i]) //把选择的选项内容展示在EditText上
            listPopupWindow.dismiss() //如果已经选择了，隐藏起来
        }
        listPopupWindow.show() //把ListPopWindow展示出来
    }

    private fun hasProxy(proxyHttp: String?): Boolean {
        return !TextUtils.isEmpty(proxyHttp) && Pattern.compile(PROXY_HTTP_MATCH).matcher(proxyHttp).matches()
    }

}