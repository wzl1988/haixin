package com.eohi.hx.ui.main

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.FragmentMyBinding
import com.eohi.hx.ui.main.model.Menu
import com.eohi.hx.utils.KillSelfService
import com.eohi.hx.utils.Preference

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/10 10:05
 */
class MyFragment : BaseFragment<BaseViewModel, FragmentMyBinding>() {
    private var accout by Preference("userid", "");
    private var username by Preference("username", "");
    private var companyname by Preference("companyname", "")
    private var firstmenus: ArrayList<Menu>? by Preference("menus", null)
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
    }

    override fun initView() {
        v.tvAccount.text = accout
        v.tvUsername.text = username
        v.tvGc.text = companyname
    }

    override fun initClick() {
        v.btnOut.setOnClickListener {
            showDialog()
        }
    }

    override fun initData() {
    }

    override fun lazyLoadData() {
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("确定要退出登录")
            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
            .setPositiveButton("确定") { dialog, which ->
                dialog.dismiss()
                accout = ""
                username = ""
                firstmenus =null
                requireContext().startService(Intent(context, KillSelfService::class.java))
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            .create().show()
    }

}