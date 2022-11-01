package com.eohi.hx.ui.work.process.card

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityCirculationCardBinding
import com.eohi.hx.ui.work.model.LzkPostModel
import com.eohi.hx.ui.work.process.viewmodel.CirculationCardViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class CirculationCardActivity :
    BaseActivity<CirculationCardViewModel, ActivityCirculationCardBinding>() {

    private var cardno = ""
    private lateinit var lzkPostModel: LzkPostModel

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
//        vm.getLzkxx(cardno)
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.ivScan.clicks {
            checkCameraPermissions(Constant.REQUEST_CODE_SCAN)
        }
        v.ivNewScan.clicks {
            checkCameraPermissions(Constant.REQUEST_CODE_SCAN_02)
        }
        v.btnPost clicks {
            when {
                TextUtils.isEmpty(v.etLzkkh.text) -> {
                    Toast.makeText(this, "请扫描流转卡！", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.etXlzkkh.text) -> {
                    Toast.makeText(this, "请扫描新流转卡！", Toast.LENGTH_SHORT).show()
                }
                TextUtils.isEmpty(v.etCcsl.text) -> {
                    Toast.makeText(this, "请输入拆出数量！", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val cklx = if (v.pass.isChecked) "拆完工数" else "拆未完工数"
                    lzkPostModel = LzkPostModel(
                        v.etLzkkh.text.toString(),
                        v.etXlzkkh.text.toString(), v.etCcsl.text.toString(), cklx, accout
                    )
                    vm.post(lzkPostModel)
                }
            }
        }
    }

    override fun initData() {
        v.tvCzr.text = username
        v.tvDate.text = DateUtil.data
    }

    override fun initVM() {
        vm.postResult.observe(this) {
            finish()
        }
        vm.lzkxxResult.observe(this) {
            if (it.isNotEmpty()) {
                it[0].apply {
                    if (v.etLzkkh.isFocused) {
                        v.tvKh.text = this.lzkkh
                        v.tvLzkzt.text = this.lzkzt
                        v.tvWpmc.text = this.wpmc
                        v.tvTh.text = this.th
                        v.tvBnsl.text = this.sybzs
                    } else if (v.etXlzkkh.isFocused) {
                        v.tvNewLzkkh.text = this.lzkkh
                        v.tvNewLzkzt.text = this.lzkzt
                        v.tvNewWpmc.text = this.wpmc
                        v.tvNewTh.text = this.th
                    }
                }
            }
        }
    }

    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions(code: Int) {
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
                code,
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
                if (v.etLzkkh.isFocused) {
                    v.etLzkkh.setText(cardno)
                } else if (v.etXlzkkh.isFocused) {
                    v.etXlzkkh.setText(cardno)
                }
                vm.getLzkxx(cardno)
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
            val result = data.getStringExtra(Intents.Scan.RESULT)
            cardno = result
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    v.etLzkkh.setText(cardno)
                    vm.getLzkxx(cardno)
                }
                Constant.REQUEST_CODE_SCAN_02 -> {
                    v.etXlzkkh.setText(cardno)
                    vm.getLzkxx(cardno)
                }

            }
        }
    }


}