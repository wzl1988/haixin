package com.eohi.hx.ui.work.process.start

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityStartWorkBinding
import com.eohi.hx.ui.work.model.KgPostModel
import com.eohi.hx.ui.work.process.viewmodel.StartWorkViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.DialogEqu
import com.eohi.hx.view.ListDialog
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class StartWorkActivity : BaseActivity<StartWorkViewModel, ActivityStartWorkBinding>() {

    private var cardno = ""
    private lateinit var listEquipment: ArrayList<String>
    private lateinit var listSbbh: ArrayList<String>
    private lateinit var listGx: ArrayList<String>
    private lateinit var listGxH: ArrayList<String>
    private lateinit var listJgdy: ArrayList<String>
    private lateinit var listJgdybh: ArrayList<String>
    private lateinit var dialogEquipment: DialogEqu
    private lateinit var hashMap: HashMap<String, String>
    private lateinit var dialogGx: ListDialog
    private lateinit var dialogJgdy: ListDialog

    private var sbbh = ""
    private var gxno = ""
    private var jgdybh = ""
    private lateinit var kgPostModel: KgPostModel

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.ivScan.clicks {
            checkCameraPermissions()
        }
        v.btnScsb clicks {
            dialogEquipment = DialogEqu(this, listEquipment, object : DialogEqu.MyListener {
                override fun refreshActivity(position: Int) {
                    v.etScsb.setText(listEquipment[position])
                    sbbh = listSbbh[position]
                    v.etJgdy.setText(listJgdy[position])
                    jgdybh = listJgdybh[position] ?: ""
                }
            })
            dialogEquipment.setOnEqu {
                vm.getPersonalEquipmentList(companyNo, accout,jgdybh)
            }
            dialogEquipment.setOnAllequ {
                getAll()
            }

            vm.getPersonalEquipmentList(companyNo, accout,jgdybh)
        }
        v.btnJgdy clicks {
            if (sbbh.isEmpty()) {
                hashMap.clear()
                hashMap["cardno"] = cardno
                hashMap["gxno"] = gxno
                vm.getJgdyNoEquipmentList(hashMap)
            } else {
                hashMap.clear()
                hashMap["equno"] = sbbh
                hashMap["cardno"] = cardno
                hashMap["gxno"] = gxno
                vm.getJgdyList(hashMap)
            }

        }
        v.btnScgx clicks {
            vm.getGxList(cardno)
        }
        v.startWork clicks {
            kgPostModel = KgPostModel(cardno, gxno, sbbh, jgdybh, accout)
            vm.startWork(kgPostModel)
        }

        v.btnSuspend.clicks {
            kgPostModel = KgPostModel(cardno, gxno, "", jgdybh, accout)
            vm.postSuspend(kgPostModel)
        }
        v
        v.btnRecovery.clicks {
            kgPostModel = KgPostModel(cardno, gxno, "", jgdybh, accout)
            vm.postRecover(kgPostModel)
        }

    }

    override fun initData() {
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.data

        listEquipment = ArrayList()
        listSbbh = ArrayList()
        listJgdy = ArrayList()
        listJgdybh = ArrayList()
        listGx = ArrayList()
        listGxH = ArrayList()
        hashMap = HashMap()


        dialogJgdy = ListDialog(this, "加工单元", listJgdy, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.etJgdy.setText(listJgdy[position])
                jgdybh = listJgdybh[position]
            }
        })

        dialogGx = ListDialog(this, "生产工序", listGx, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.etScgx.setText(listGx[position])
                gxno = listGxH[position]
            }

        })

//        vm.getKgInfo("210902000001")

    }

    private fun initMap() {
        hashMap["companycode"] = companyNo
        hashMap["scxbh"] = jgdybh
        hashMap["userid"] = accout
    }

    //获取全部设备
    private fun getAll() {
        initMap()
        vm.getAllEquipmentList(hashMap)
    }

    override fun initVM() {
        vm.kgpostresult.observe(this) {
            finish()
            showShortToast("提交成功")
        }

        vm.suspendresult.observe(this){
            finish()
            showShortToast("暂停成功")
        }

        vm.recoverresult.observe(this){
            finish()
            showShortToast("恢复成功")
        }

        vm.gxList.observe(this) { it ->
            listGx.clear()
            it.onEach {
                listGx.add(it.gxms)
                listGxH.add(it.gxh)
            }
            dialogGx.show()
            dialogGx.refreshList(listGx)
        }
        vm.jgdyList.observe(this) { it ->
            listJgdy.clear()
            listJgdybh.clear()
            it.onEach {
                listJgdybh.add(it.jgdybh)
                listJgdy.add(it.jgdymc)
            }
            dialogJgdy.show()
            dialogJgdy.refreshList(listJgdy)
        }
        vm.kgInfoList.observe(this) {
            if (it.isNotEmpty()) {
                it[0].apply {
                    v.tvGdh.text = this.rwdh
                    gxno = this.rwdh
                    v.tvWph.text = this.wph
                    v.tvWpmc.text = this.wpmc
                    v.tvGg.text = this.gg
                    v.tvDw.text = this.jldw
                    v.tvScsl.text = this.sybzs
                    v.etJgdy.setText(this.jgdymc)
                    this@StartWorkActivity.jgdybh = this.jgdybh
                }
            }
        }
        vm.equipmentList.observe(this) { it ->
            listEquipment.clear()
            listSbbh.clear()
            listJgdy.clear()
            listJgdybh.clear()
            it.onEach {
                listSbbh.add(it.sbbh?:"")
                listEquipment.add(it.sbmc?:"")
                listJgdybh.add(it.scxbh)
                listJgdy.add(it.scxmc)
            }
            dialogEquipment.show()
            dialogEquipment.refreshList(listEquipment)
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
                cardno = result
                v.etLzkh.setText(cardno)
                vm.getKgInfo(cardno)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT).trim { it <= ' ' }
                    cardno = result
                    v.etLzkh.setText(cardno)
                    vm.getKgInfo(cardno)
                }

            }
        }
    }
}