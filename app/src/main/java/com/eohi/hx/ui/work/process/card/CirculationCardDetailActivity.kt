package com.eohi.hx.ui.work.process.card

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityCirculationCardDetailBinding
import com.eohi.hx.ui.work.adapter.LzkSubAdapter
import com.eohi.hx.ui.work.model.LZKSubListModel
import com.eohi.hx.ui.work.process.viewmodel.CirculationCardViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class CirculationCardDetailActivity :
    BaseActivity<CirculationCardViewModel, ActivityCirculationCardDetailBinding>() {

    private var cardno = ""
    private lateinit var list: ArrayList<LZKSubListModel>
    private lateinit var adapter: LzkSubAdapter

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        list = ArrayList()
        adapter = LzkSubAdapter(this, list)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

//        vm.getLzkxx(cardno)
//        vm.getSubList(cardno)
//        vm.getCLXH(cardno)
//        vm.getGx(cardno)
    }

    override fun initClick() {
        v.ivBack clicks {
            finish()
        }
        v.ivScan.clicks {
            checkCameraPermissions()
        }
    }

    override fun initData() {
    }

    override fun initVM() {
        vm.lzkxxResult.observe(this) {
            if (it.size > 0) {
                it[0].apply {
                    v.tvWpmc.text = wpmc
                    v.tvGg.text = gg
                    v.tvLzkzt.text = lzkzt
                    v.tvRwdh.text = rwdh
                    v.tvKhmc.text = khmc
                    v.tvJhsl.text = scsl
                    v.tvCzr.text = cjyhid
                    v.tvCjrq.text = cjrq
                }
            }
        }
        vm.subListResult.observe(this) {
            if (it.size > 0) {
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
        vm.clxhResult.observe(this) {
            if (it.size > 0) {
                it[0].apply {
                    v.tvClpc.text = clph
                    v.tvGys.text = gysmc
                    v.tvClgg.text = gg
                    v.tvClwph.text = clwph
                    v.tvClmc.text = clmc
                    v.tvClth.text = clth
                    v.tvXhsl.text = xhsl
                }
            }
        }
        vm.gxResult.observe(this) {
            if (it.size > 0) {
                it[0].apply {
                    v.tvGxh.text = gxh
                    v.tvGxmc.text = gxms
                    v.tvBgrmc.text = bgrxm
                    v.tvBgsl.text = bgsl
                    v.tvBgsj.text = bgsj
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
                v.etLzkkh.setText(cardno)
                vm.getLzkxx(cardno)
                vm.getSubList(cardno)
                vm.getCLXH(cardno)
                vm.getGx(cardno)
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
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    cardno = result
                    v.etLzkkh.setText(cardno)
                    vm.getLzkxx(cardno)
                    vm.getSubList(cardno)
                    vm.getCLXH(cardno)
                    vm.getGx(cardno)
                }

            }
        }
    }

}