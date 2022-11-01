package com.eohi.hx.ui.work.quality.unqualified

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityUnqualifiedReportBinding
import com.eohi.hx.ui.work.model.BlxxBean
import com.eohi.hx.ui.work.model.UnQualifiedPostModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.view.MultiListDialog
import com.eohi.hx.widget.clicks
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/*
* 不合格品登记
* */
class UnQualifiedReportActivity :
    BaseActivity<UnQualifiedReportViewModel, ActivityUnqualifiedReportBinding>(),
    EasyPermissions.PermissionCallbacks {

    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()
    private var cardno = ""
    private var gxno = ""//工序号
    private var lzkkh = ""//流转卡卡号
    private lateinit var dialogGx: ListDialog
    private lateinit var listGx: ArrayList<String>
    private lateinit var listGxH: ArrayList<String>
    private lateinit var dialogPerson: ListDialog
    private lateinit var listPerson: ArrayList<String>
    private lateinit var listPersonBh: ArrayList<String>
    private var produceuserid = accout//生产人员工号

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        v.tvScry.text = username
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.audioTime
        vm.getBlxx()
    }

    override fun initClick() {
        v.ivBack clicks {
            finish()
        }
        v.tvLzkbh clicks {
            checkCameraPermissions()
        }
        v.btnBlxx clicks {
            val tempList = ArrayList<String>()
            blxxList.forEach {
                tempList.add(it.XXSM)
            }
            val dialog =
                MultiListDialog(this, "不良现象", tempList, object : MultiListDialog.MyListener {
                    override fun refreshActivity(listPosition: ArrayList<Int>) {
                        postBlxxList.clear()
                        var str = ""
                        listPosition.forEach {
                            str += blxxList[it].XXSM + ","
                            postBlxxList.add(blxxList[it])
                        }
                        v.tvBlxx.text = str.substring(0, str.length - 1)
                    }
                })
            dialog.show()
            dialog.hideSearch()
        }
        v.btnScgx clicks {
            if (TextUtils.isEmpty(cardno)) {
                Toast.makeText(this, "请扫码获取流转卡信息！", Toast.LENGTH_SHORT).show()
            } else {
                vm.getGx(cardno)
            }
        }
        v.btnPost clicks {
            when {
                TextUtils.isEmpty(v.tvLzkbh.text.toString()) -> {
                    Toast.makeText(this, "请扫码获取流转卡信息！", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.tvBlxx.text.toString()) -> {
                    Toast.makeText(this, "请选择不良现象！", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.etBhgsl.text.toString()) -> {
                    Toast.makeText(this, "请输入不合格数量！", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.tvScgx.text.toString()) -> {
                    Toast.makeText(this, "请选择生产工序！", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val unQualifiedPostModel = UnQualifiedPostModel()
                    unQualifiedPostModel.lzkkh = lzkkh
                    unQualifiedPostModel.bhgsl = v.etBhgsl.text.toString().toInt()
                    unQualifiedPostModel.gxh = gxno
                    unQualifiedPostModel.gxsm = v.tvScgx.text.toString()
                    unQualifiedPostModel.zrrgh = produceuserid
                    unQualifiedPostModel.zrrxm = v.tvScry.text.toString()
                    unQualifiedPostModel.zjryid = accout
                    unQualifiedPostModel.zjry = username
                    unQualifiedPostModel.bz = v.etBz.text.toString()
                    unQualifiedPostModel.swrq = DateUtil.audioTime
                    unQualifiedPostModel.blxx = postBlxxList
                    vm.post(unQualifiedPostModel)
                }
            }
        }
        v.btnScry clicks {
            vm.getPerson(companyNo, "")
        }
    }

    override fun initData() {
        listGx = ArrayList()
        listGxH = ArrayList()
        listPerson = ArrayList()
        listPersonBh = ArrayList()
        dialogGx = ListDialog(this, "生产工序", listGx, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvScgx.text = listGx[position]
                gxno = listGxH[position]
            }
        })
        dialogPerson = ListDialog(this, "人员选择", listPerson, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvScry.text = listPerson[position]
                produceuserid = listPersonBh[position]
            }
        })
    }

    override fun initVM() {
        vm.postResult.observe(this) {
            Toast.makeText(this, "执行成功！", Toast.LENGTH_SHORT).show()
        }
        vm.personResult.observe(this) { it ->
            listPerson.clear()
            listPersonBh.clear()
            it.onEach {
                listPerson.add(it.ygxm)
                listPersonBh.add(it.ygbh)
            }
            dialogPerson.show()
            dialogPerson.refreshList(listPerson)
            dialogPerson.hideSearch()
        }
        vm.gxResult.observe(this) { it ->
            listGx.clear()
            it.onEach {
                listGx.add(it.gxms)
                listGxH.add(it.gxh)
            }
            dialogGx.show()
            dialogGx.refreshList(listGx)
            dialogGx.hideSearch()
        }
        vm.blxxResult.observe(this) {
            blxxList.addAll(it)
        }
        vm.lzkDetailResult.observe(this) {
            it[0].apply {
                v.tvCpmc.text = wpmc
                v.tvCpth.text = th
                v.tvXqsl.text = bzs
                this@UnQualifiedReportActivity.lzkkh = lzkkh
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                v.tvLzkbh.text = data?.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!
                cardno = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!
                vm.getLzkDetail(
                    data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!
                )
            }
        }
    }

    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
            startActivityForResult(intent, 1)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), IncomingCheckActivity.RC_CAMERA, *perms
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        val intent = Intent(this, ScannerActivity::class.java)
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
        startActivityForResult(intent, 1)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("确定", "重要提示", "请去设置里开启权限！") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
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
                v.tvLzkbh.text = result
                cardno = result
                vm.getLzkDetail(
                    result
                )
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}