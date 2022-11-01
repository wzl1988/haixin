package com.eohi.hx.ui.work.tooling.check

import android.Manifest
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityToolingCheckBinding
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import kotlinx.android.synthetic.main.common_toolbar.view.*
import org.json.JSONException
import org.json.JSONObject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class ToolingCheck : BaseActivity<BaseViewModel, ActivityToolingCheckBinding>(),
    EasyPermissions.PermissionCallbacks {

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.root.title.text = "工装检查"
        v.root.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun initClick() {
        v.scan.setOnClickListener {
            checkCameraPermissions()
        }
    }

    override fun initData() {
        for (i in 0..1) {
            val view: View =
                LayoutInflater.from(this).inflate(R.layout.item_tooling_check, null, false)
            v.container.addView(view)
        }
    }

    override fun initVM() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    try {
                        //String转JSONObject
                        val jsonstr = JSONObject(result)
                        //取数据
                        val code = jsonstr["data"].toString()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
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
}