package com.eohi.hx.ui.work.process.material

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.ui.work.model.RemovalSubModel
import com.eohi.hx.ui.work.process.viewmodel.MaterialViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MaterialRemovalActivity : BaseActivity<MaterialViewModel,com.eohi.hx.databinding.ActivityMaterialRemovalBinding>() {
    val yylist = ArrayList<String>()
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
    }

    override fun initClick() {
        v.ivBack.clicks {
            finish()
        }
        v.ivScan.clicks {
            checkCameraPermissions(Constant.REQUEST_CODE_SCAN)
        }
        v.ivNewScan.clicks {
            checkCameraPermissions(Constant.REQUEST_CODE_SCAN_02)
        }
        v.etYy.clicks {
            if(yylist.isEmpty()){
                vm.getCkyy()
            }else{
                showListPopulWindow(v.etYy,yylist.toTypedArray())
            }
        }
        v.btnSubmit.clicks {
            if(v.etKh.text.isEmpty()||v.etKh2.text.isEmpty()){
                showShortToast("??????????????????????????????")
                return@clicks
            }
            if(v.etYy.text.isEmpty()){
                showShortToast("????????????????????????")
                return@clicks
            }

            if(v.etSl.text.isEmpty()){
                showShortToast("????????????????????????")
                return@clicks
            }

            vm.submit(RemovalSubModel(v.etKh.text.toString(),
                v.etKh2.text.toString(),v.etSl.text.toString(), accout,v.etYy.text.toString()))

        }
    }

    override fun initData() {
        v.tvCzr.text = BaseFragment.username
        v.tvRq.text = DateUtil.data
    }

    override fun initVM() {
        vm.yylist.observe(this, Observer {
            if(it.isNotEmpty()){
                it.forEach {
                    yylist.add(it.yysm)
                }
                showListPopulWindow(v.etYy,yylist.toTypedArray())
            }
        })

        vm.ckxx.observe(this, Observer {
            if(it.isNotEmpty()){
                val info=it[0]
                v.tvCpmc.text =info.wpmc
                v.tvTh.text = info.th
                v.tvGg.text = info.gg
                v.tvGys.text = info.gysmc
                v.tvYSl.text = info.sysl.toString()
                v.etKh.setText(info.clbzh)
                v.etKh2.requestFocus()
            }
        })

        vm.newckxx.observe(this, Observer {
            if(it.isNotEmpty()){
                val info=it[0]
                v.tvCpmc2.text = info.wpmc
                v.tvTh2.text = info.th
                v.tvGg2.text = info.gg
                v.tvGys2.text = info.gysmc
                v.tvSysl.text =info.sysl.toString()
                v.etKh2.setText(info.clbzh)
            }
        })

        vm.submitresult.observe(this, Observer {
            showShortToast("????????????")

        })

    }


    private fun showListPopulWindow(mEditText: TextView, list: Array<String>) {
        val  listPopupWindow = ListPopupWindow(mContext)
        listPopupWindow.setAdapter(ArrayAdapter(mContext, R.layout.pop_item, list)) //???android???????????????????????????????????????
        listPopupWindow.anchorView = mEditText //???????????????????????????????????????mEditText?????????
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(mContext, R.color.white)))
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //?????????????????????
            mEditText.setText(list[i]) //?????????????????????????????????EditText???
            listPopupWindow.dismiss() //????????????????????????????????????
        }
        listPopupWindow.show() //???ListPopWindow????????????
    }


    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions(code:Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //?????????
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "??????")
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
        // ?????????????????????
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }
    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION ) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                when{
                    v.etKh.isFocused->{
                        vm.getCkxx(result, accout)
                    }
                    v.etKh2.isFocused->{
                        vm.getNewCKxx(result, accout)
                    }
                }
            }
        }
    }

    override fun onPause() {
        //??????????????????
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val result = data.getStringExtra(Intents.Scan.RESULT)
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    vm.getCkxx(result, accout)
                }
                Constant.REQUEST_CODE_SCAN_02->{
                    vm.getNewCKxx(result, accout)
                }

            }
        }
    }


}