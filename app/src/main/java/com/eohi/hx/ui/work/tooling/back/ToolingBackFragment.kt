package com.eohi.hx.ui.work.tooling.back

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentToolingBackBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.tooling.viewmodel.ToolingBackViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/13 14:53
 */
class ToolingBackFragment : BaseFragment<ToolingBackViewModel, FragmentToolingBackBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.reasonlist.observe(this, Observer {
            if (it.isNotEmpty()) {
                val list = ArrayList<String>()
                it.forEach {
                    list.add(it.rkyy)
                }
                showListPopulWindow(v.tvGhyy, list.toTypedArray())
            }
        })
        vm.toolinfo.observe(this, Observer {
            if (it.isNotEmpty()) {
                v.tvGg.text = it[0].gg
                v.tvCkh.text = it[0].ckh
                if (v.tvKwh.text.isEmpty())
                    v.tvKwh.text = it[0].kwh
                v.tvGzdd.text = it[0].dqszwz
                it[0].sqrbh = accout
                it[0].slr = username
                it[0].lyyy = v.tvGhyy.text.toString()
                it[0].kwh = v.tvKwh.text.toString()
                App.post(EventMessage(EventCode.DATA, "", "", 0, it[0]))
            }
        })

        vm.ckh.observe(this, Observer {
            if (it.isNotEmpty()) {
                v.tvCkh.text = it[0].ckh
            }
        })

    }

    override fun initView() {
    }

    override fun initClick() {
        v.rlGhyy.clicks {
            vm.getGhyy()
        }
        v.ivKwh.clicks {
            checkCameraPermissions(Constant.REQUEST_CODE_SCAN)
        }
        v.ivScan.clicks {
            checkCameraPermissions(Constant.REQUEST_CODE_SCAN_02)
        }
    }

    override fun initData() {
        v.tvCzy.text = username
        v.tvSlr.text = username
        v.tvCzsj.text = DateUtil.dataTime
    }

    override fun lazyLoadData() {
    }

    private fun showListPopulWindow(mEditText: TextView, list: Array<String>) {
        val listPopupWindow = ListPopupWindow(mContext)
        listPopupWindow.setAdapter(
            ArrayAdapter(
                mContext,
                R.layout.pop_item,
                list
            )
        ) //用android内置布局，或设计自己的样式
        listPopupWindow.anchorView = mEditText //以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        )
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //设置项点击监听
            mEditText.setText(list[i]) //把选择的选项内容展示在EditText上
            listPopupWindow.dismiss() //如果已经选择了，隐藏起来
        }
        listPopupWindow.show() //把ListPopWindow展示出来
    }


    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions(code: Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(mContext, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.`in`, R.anim.out)
            val intent = Intent(activity, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            activity?.let {
                ActivityCompat.startActivityForResult(
                    it,
                    intent,
                    code,
                    optionsCompat.toBundle()
                )
            }
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
        activity?.registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }

                when {
                    v.etKwh.isFocused -> {
                        v.etKwh.setText(result)
                        v.tvKwh.text = result
                        vm.getCkh(result)
                        v.etGzbh.requestFocus()
                    }
                    v.etGzbh.isFocused -> {
                        v.etGzbh.setText(result)
                        vm.getToolInfo(result, companyNo)
                    }
                }

            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity?.unregisterReceiver(scanReceiver)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val result = data.getStringExtra(Intents.Scan.RESULT)
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    v.etKwh.setText(result)
                    v.tvKwh.text = result
                    vm.getCkh(result)
                }
                Constant.REQUEST_CODE_SCAN_02 -> {
                    v.etGzbh.setText(result)
                    vm.getToolInfo(result, companyNo)
                }
            }
        }
    }


}