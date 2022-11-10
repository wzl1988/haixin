package com.eohi.hx.ui.work.quality.unqualified

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.text.TextUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityUnqualifiedReportBinding
import com.eohi.hx.ui.work.model.BlxxBean
import com.eohi.hx.ui.work.model.Items
import com.eohi.hx.ui.work.model.UnQualifiedPostModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.ui.work.quality.unqualified.adapter.MxAdapter
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.Extensions.showShortToast
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
    EasyPermissions.PermissionCallbacks ,MxAdapter.DeleteListener, MxAdapter.BlyyListener,MxAdapter.SlListener,MxAdapter.SmListener{

    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()
    private var cardno = ""
    private var gxno = ""//工序号
    private var lzkkh = ""//流转卡卡号
    private var jgdybh =""
    private var jgdymc =""
    private var sbbh =""
    private var sbmc =""
    private var rwdh =""
    private lateinit var dialogGx: ListDialog
    private lateinit var listGx: ArrayList<String>
    private lateinit var listGxH: ArrayList<String>
    private lateinit var dialogPerson: ListDialog
    private lateinit var listPerson: ArrayList<String>
    private lateinit var listPersonBh: ArrayList<String>
    private var produceuserid = accout//生产人员工号
    private var scr =""
    private var mxList = ArrayList<Items>()
    private lateinit var mxAdapter: MxAdapter
    private var zs = 0
    private var slPosition = 0
    private var temp = 0
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
//        cardno = "210902000001"
//        lzkkh= "210902000001"
//        v.tvLzkbh.text= "210902000001"
//        vm.getLzkDetail("210902000001")
    }

    override fun initClick() {
        v.ivBack clicks {
            finish()
        }
        v.tvLzkbh clicks {
            checkCameraPermissions()
        }
//        v.btnBlxx clicks {
//            val tempList = ArrayList<String>()
//            blxxList.forEach {
//                tempList.add(it.XXSM)
//            }
//            val dialog =
//                MultiListDialog(this, "不良现象", tempList, object : MultiListDialog.MyListener {
//                    override fun refreshActivity(listPosition: ArrayList<Int>) {
//                        postBlxxList.clear()
//                        var str = ""
//                        listPosition.forEach {
//                            str += blxxList[it].XXSM + ","
//                            postBlxxList.add(blxxList[it])
//                        }
//                        v.tvBlxx.text = str.substring(0, str.length - 1)
//                    }
//                })
//            dialog.show()
//            dialog.hideSearch()
//        }
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
                TextUtils.isEmpty(v.etBhgsl.text.toString()) -> {
                    Toast.makeText(this, "请输入不合格数量！", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.tvScgx.text.toString()) -> {
                    Toast.makeText(this, "请选择生产工序！", Toast.LENGTH_SHORT).show()
                }
                v.etBhgsl.text.toString().toInt() < 0 -> {
                    showShortToast("不合格数量不能小于0")
                }
                zs != v.etBhgsl.text.toString().toInt() -> {
                    showShortToast("不合格明细数量和总不合格数量不一致请修改确认")
                }
                else -> {
                    val unQualifiedPostModel = UnQualifiedPostModel()
                    unQualifiedPostModel.lzkkh = lzkkh
                    unQualifiedPostModel.gsh = companyNo
                    unQualifiedPostModel.bhgzsl = v.etBhgsl.text.toString().toInt()
                    if(null != jgdybh)
                    unQualifiedPostModel.jgdybh = jgdybh
                    if(null != jgdymc)
                    unQualifiedPostModel.jgdymc = jgdymc
                    unQualifiedPostModel.sbbh= sbbh
                    unQualifiedPostModel.sbmc = sbmc
                    unQualifiedPostModel.gxbh = gxno
                    unQualifiedPostModel.gxmc = v.tvScgx.text.toString()
                    unQualifiedPostModel.scfs ="个人"
                    unQualifiedPostModel.bz = v.etBz.text.toString()
                    unQualifiedPostModel.rwdh = rwdh
                    unQualifiedPostModel.cjr = username
                    unQualifiedPostModel.cjrid = accout
                    unQualifiedPostModel.scrid =produceuserid
                    unQualifiedPostModel.scr = scr
                    unQualifiedPostModel.items = mxList
                    vm.post(unQualifiedPostModel)
                }
            }
        }
        v.btnScry clicks {
            vm.getPerson(companyNo, "")
        }

        v.btnAdd.clicks {
            if(v.tvScgx.text.isEmpty()){
                showShortToast("请先选择生产工序")
                return@clicks
            }
            if(v.tvScry.text.isEmpty()){
                showShortToast("请先选择生产人员")
                return@clicks
            }
            var item = Items()
            item.zrgxh = gxno
            item.zrgxm = v.tvScgx.text.toString()
            item.scrid = produceuserid
            item.scr = scr
            mxList.add(item)
            mxAdapter.notifyItemChanged(mxList.size - 1)

        }
    }

    override fun initData() {
        listGx = ArrayList()
        listGxH = ArrayList()
        listPerson = ArrayList()
        listPersonBh = ArrayList()

        mxAdapter = MxAdapter(this, mxList).apply {
            setDeleteListener(this@UnQualifiedReportActivity)
            setBlyyListener(this@UnQualifiedReportActivity)
            setSlListener(this@UnQualifiedReportActivity)
            setSmLis(this@UnQualifiedReportActivity)
        }
        v.rvMx.apply {
            layoutManager = LinearLayoutManager(this@UnQualifiedReportActivity)
            adapter = mxAdapter
        }
        dialogGx = ListDialog(this, "生产工序", listGx, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvScgx.text = listGx[position].substringBefore("(")
                gxno = listGxH[position]
            }
        })
        dialogPerson = ListDialog(this, "人员选择", listPerson, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvScry.text = listPerson[position]
                produceuserid = listPersonBh[position]
                scr = listPerson[position]
            }
        })
    }

    override fun initVM() {
        vm.postResult.observe(this) {
            Toast.makeText(this, "执行成功！", Toast.LENGTH_SHORT).show()
            finish()
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
                listGx.add(it.gxms+"("+it.gxh+")")
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
                v.tvCpth.text = gg
                v.tvXqsl.text = bzs.toString()
                v.tvWph.text = wph
                v.tvJgdy.text = jgdymc
                this@UnQualifiedReportActivity.lzkkh = lzkkh
                this@UnQualifiedReportActivity.jgdybh = jgdybh
                this@UnQualifiedReportActivity.jgdymc = jgdymc
                this@UnQualifiedReportActivity.sbbh = sbbh
                this@UnQualifiedReportActivity.sbmc = sbmc
                this@UnQualifiedReportActivity.rwdh = rwdh
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                v.tvLzkbh.text = data?.getStringExtra(Constant.EXTRA_RESULT_CONTENT)?.trim { it <= ' ' }!!
                cardno = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT).trim { it <= ' ' }!!
                vm.getLzkDetail(
                    data.getStringExtra(Constant.EXTRA_RESULT_CONTENT).trim { it <= ' ' }!!
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

    override fun delete(position: Int) {
        showAlertDialog("确定", "重要提示", "是否删除") {
            zs -= mxList[position].blsl
            mxList.removeAt(position)
            mxAdapter.notifyDataSetChanged()
        }
    }

    override fun setBlyy(position: Int) {
        val tempList = ArrayList<String>()
        blxxList.forEach {
            tempList.add(it.XXSM)
        }
        ListDialog(this, "不良现象", tempList, object : ListDialog.MyListener {
            override fun refreshActivity(i: Int) {
                mxList[position].blxxbm = blxxList[i].XXBM
                mxList[position].blxx = blxxList[i].XXSM
                mxAdapter.notifyItemChanged(position)
            }
        }).apply {
            show()
            hideSearch()
        }
    }

    override fun setSl(position: Int, s: String) {
        if (slPosition != position) {
            slPosition = position
            temp = 0
        }
        mxList[position].blsl = s.toInt()
        zs -= temp
        zs += s.toInt()
        temp = s.toInt()
    }

    override fun setSm(position: Int, s: String) {
        mxList[position].sm = s
    }

}