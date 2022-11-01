package com.eohi.hx.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.text.Editable
import android.text.TextUtils
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityLoginBinding
import com.eohi.hx.ui.login.model.CompanyModel
import com.eohi.hx.ui.login.model.LoginModel
import com.eohi.hx.ui.main.MainActivity
import com.eohi.hx.ui.main.model.Menu
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.utils.dp2Px
import com.eohi.hx.view.DialogSettingHttp
import com.eohi.hx.widget.clicks

class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    private var accout by Preference<String>("userid", "")
    private var username by Preference("username", "")
    private var companyno by Preference("companyNo", "")
    private var companyname by Preference("companyname", "")
    var companyliststr: ArrayList<String>? = null
    var companyList: ArrayList<CompanyModel>? = null
    private var menus: ArrayList<Menu>? by Preference("menus", ArrayList<Menu>())

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        if (!TextUtils.isEmpty(accout)) {
            v.etAccount.text = Editable.Factory.getInstance().newEditable(accout)
        }
        v.btnLogin.setBackColor(Color.parseColor("#537BFE"))
        v.btnLogin.setBoderWidth(0.5f.dp2Px(this))
        v.btnLogin.setBorderColor(Color.parseColor("#350C1755"))
        v.btnLogin.setRadius(20.5f.dp2Px(this))
        v.btnLogin.setTextColor(Color.WHITE)

    }

    override fun initClick() {
        v.tvCompany.setOnClickListener {
            vm.getCompany()
        }

        v.btnLogin.setOnClickListener {
            val accout = v.etAccount.text.toString()
            val psw = v.etPsw.text.toString()
            if (TextUtils.isEmpty(accout) || TextUtils.isEmpty(psw)) {
                ToastUtil.showToast(this, "用户名或密码不能为空！")
                return@setOnClickListener
            }
            if (v.tvCompany.text.isEmpty()) {
                ToastUtil.showToast(this, "请先选择公司！")
                return@setOnClickListener
            }

            val loginmodel = LoginModel(accout, psw, "PDA")
            vm.getLoginResult(loginmodel, true)
        }
        v.ibSetting.clicks {
            val d = DialogSettingHttp(mContext)
            d.show()
        }
    }

    override fun initData() {
        v.tvCompany.text = companyno
    }

    override fun initVM() {
        vm.companylist.observe(this) {
            if (it.isNotEmpty()) {
                companyList = it
                companyliststr = ArrayList<String>()
                for (i in it.indices) {
                    companyliststr!!.add(it[i].Name)
                }
                showCompanyList()
            }
        }

        vm.loginresut.observe(this) {
            if (it.code == 1000) {
                username = it.data.list[0].UserName
                menus = it.menus as ArrayList<Menu>
                accout = v.etAccount.text.toString()
                redirectTo()
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }

        }
    }

    private fun showCompanyList() {
        if (companyliststr == null || companyliststr!!.isEmpty()) return
        var items = companyliststr!!.toTypedArray()
        val builder = AlertDialog.Builder(mContext, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.tvCompany.text = companyliststr!![which]
            companyname = companyliststr!![which]
            companyno = companyList!![which].Code
        }
        builder.create().show()
    }

    private fun redirectTo() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

}