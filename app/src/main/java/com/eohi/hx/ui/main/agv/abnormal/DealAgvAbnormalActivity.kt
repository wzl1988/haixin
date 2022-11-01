package com.eohi.hx.ui.main.agv.abnormal

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityDealAgvabnormalBinding
import com.eohi.hx.event.Event
import com.eohi.hx.event.EventCode
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalListBean
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalPostBean
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 *@author: YH
 *@date: 2022/1/13
 *@desc:对应状态3,7
 */
class DealAgvAbnormalActivity :
    BaseActivity<AgvAbnormalViewModel, ActivityDealAgvabnormalBinding>() {

    companion object {
        const val REQUEST_MQSZWZ = 0X111
    }

    private lateinit var agvAbnormalListBean: AgvAbnormalListBean

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        if (intent != null && intent.extras?.containsKey("AgvAbnormalListBean")!!) {
            agvAbnormalListBean =
                intent.extras?.getSerializable("AgvAbnormalListBean") as AgvAbnormalListBean
            v.rwid.text = agvAbnormalListBean.rwid
            v.ywlx.text = agvAbnormalListBean.ywlx
            v.rwzt.text = agvAbnormalListBean.rwzt.toString()
            v.ch.text = agvAbnormalListBean.ch
            v.qd.text = agvAbnormalListBean.qd
            v.zd.text = agvAbnormalListBean.zd
            v.cjr.text = agvAbnormalListBean.cjrid
            v.cjsj.text = agvAbnormalListBean.cjsj
            v.zhfksj.text = agvAbnormalListBean.ZHZXSJ
        }
    }

    override fun initClick() {
        v.ivBack clicks {
            finish()
        }
        v.btnCancel clicks {
            finish()
        }
        v.btnPost clicks {
            if (TextUtils.isEmpty(v.etMqszdw.text.toString())) {
                showShortToast("请扫码或输入目前所在点位")
            } else {
                val postBean =
                    AgvAbnormalPostBean(agvAbnormalListBean.rwid, v.etMqszdw.text.toString(), "1")
                vm.dealAgvAbnormal(postBean)
            }
        }
        v.ivScan clicks {
            checkCameraPermissions(REQUEST_MQSZWZ)
        }
    }

    override fun initData() {

    }

    override fun initVM() {
        vm.dealResult.observe(this) {
            showShortToast(it[0].returntext)
            Event.getInstance().post(EventCode.REFRESH)
            finish()
        }
    }

    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions(requestCode: Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
            startActivityForResult(intent, requestCode)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), IncomingCheckActivity.RC_CAMERA, *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
            when (requestCode) {
                REQUEST_MQSZWZ -> {
                    v.etMqszdw.setText(result)
                }
            }
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
                when {
                    v.etMqszdw.isFocused -> {
                        v.etMqszdw.setText(result)
                    }
                }
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}