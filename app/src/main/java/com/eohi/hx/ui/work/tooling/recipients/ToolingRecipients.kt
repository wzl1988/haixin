package com.eohi.hx.ui.work.tooling.recipients

import android.Manifest
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityToolingRecipientsBinding
import com.eohi.hx.utils.Constant.KEY_IS_CONTINUOUS
import com.eohi.hx.utils.Constant.KEY_TITLE
import com.eohi.hx.utils.Constant.RC_CAMERA
import com.eohi.hx.utils.Constant.REQUEST_CODE_SCAN
import com.eohi.hx.utils.Constant.isContinuousScan
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import kotlinx.android.synthetic.main.common_toolbar.view.*
import org.json.JSONException
import org.json.JSONObject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
//未启用
class ToolingRecipients : BaseActivity<BaseViewModel, ActivityToolingRecipientsBinding>(),
    PermissionCallbacks {

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.root.title.text = "工装领用"
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

    }

    override fun initVM() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_CODE_SCAN -> {
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

    @AfterPermissionGranted(RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(KEY_TITLE, "扫码")
            intent.putExtra(KEY_IS_CONTINUOUS, isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), RC_CAMERA, *perms
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

}