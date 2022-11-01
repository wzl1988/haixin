package com.eohi.hx.ui.work.tooling.handover

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityToolingHandoverBinding
import com.eohi.hx.ui.work.tooling.model.ToolhandoverSubModel
import com.eohi.hx.ui.work.tooling.viewmodel.ToolingHandoverViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import kotlinx.android.synthetic.main.common_toolbar.view.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class ToolingHandover : BaseActivity<ToolingHandoverViewModel, ActivityToolingHandoverBinding>(),
    EasyPermissions.PermissionCallbacks {
    private lateinit var personlist: ArrayList<String>
    private lateinit var personNum: ArrayList<String>
    private lateinit var dialogPerson: ListDialog
    var ygbh = accout
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.root.title.text = "工装交接"
        v.root.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun initClick() {
        v.scan.setOnClickListener {
            checkCameraPermissions()
        }
        v.rlJsr.clicks {
            vm.getPersonList(companyNo, "")
        }
        v.tvSubmit.clicks {
            if (v.tvGzbh.text.isEmpty()) {
                showShortToast("工装编号不能为空")
                return@clicks
            }
            if (v.etJswz.text.isEmpty()) {
                showShortToast("接收位置不能为空")
                return@clicks
            }
            vm.submitHandover(
                ToolhandoverSubModel(
                    accout,
                    v.etJswz.text.toString(), companyNo, v.tvGzbh.text.toString(), ygbh
                )
            )
        }
    }

    override fun initData() {
        v.tvJsr.text = username
        v.tvCzy.text = username
        v.tvCzsj.text = DateUtil.dataTime
        personlist = ArrayList()
        personNum = ArrayList()
        dialogPerson = ListDialog(mContext, "人员选择", personlist, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvJsr.text = personlist[position]
                ygbh = personNum[position]
            }
        })
        dialogPerson.onSearchClick {
            vm.getPersonList(companyNo, it)
        }
    }

    override fun initVM() {
        vm.handoverinfo.observe(this, Observer {
            if (it.isNotEmpty()) {
                v.tvGzmc.text = it[0].wpmc
                v.tvGg.text = it[0].gg
                v.tvGzdd.text = it[0].dqszwz
                v.tvDqszsb.text = it[0].dqszsb
                v.tvDqlyr.text = it[0].dqbgr
            }
        })
        vm.personlist.observe(this, Observer {
            if (it.isNotEmpty()) {
                personlist.clear()
                personNum.clear()
                it.forEach {
                    personlist.add(it.xm)
                    personNum.add(it.ygbh)
                }
                dialogPerson.show()
                dialogPerson.refreshList(personlist)

            }

        })
        vm.result.observe(this, Observer {
            v.tvGzbh.text = ""
            v.etJswz.setText("")
            showShortToast("提交成功")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    v.tvGzbh.text = result
                    vm.getToolhandoverInfo(result, companyNo)
                }
            }
        }
    }

    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                Constant.REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }


    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.tvGzbh.text = result
                vm.getToolhandoverInfo(result, companyNo)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }


}