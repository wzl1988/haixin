package com.eohi.hx.ui.work.quality.repair

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityRepairDisposeBinding
import com.eohi.hx.ui.work.model.RepairPostModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

//返修处理
class RepairDisposeActivity : BaseActivity<RepairDisposeViewModel,ActivityRepairDisposeBinding>() {
    private lateinit var dialogPerson: ListDialog
    private var listPerson = ArrayList<String>()
    private var sjr = username
    private var sjrid= accout
    override fun isNeedEventBus(): Boolean {
      return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

    }
    @SuppressLint("ClickableViewAccessibility")
    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.tvTmh.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.tvTmh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.tvTmh.width
                    - v.tvTmh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(1001)
                }
                return false
            }
        })
        v.tvRy.clicks {
            vm.getPerson(companyNo, "")
        }
        v.btnPost.clicks {
            if(v.tvTmh.text.isEmpty()){
                showShortToast("条码号不能为空")
                return@clicks
            }
            if(v.tvWph.text.isEmpty()){
                showShortToast("物品号不能为空")
                return@clicks
            }
            if(v.etFxgs.text.isEmpty()){
                showShortToast("请输入返修工时")
                return@clicks
            }

            vm.postRepairModel(RepairPostModel(
                v.tvBgjlslh.text.toString(),v.tvBgsl.text.toString(),
                username, accout,DateUtil.data,v.etFxgs.text.toString(),
                v.etFxsm.text.toString(),v.tvGg.text.toString(),
                v.tvGltmh.text.toString(),v.tvLzkh.text.toString(),
                v.tvRwdh.text.toString(),v.tvScddh.text.toString(),
                v.tvTmh.text.toString(),v.tvWph.text.toString(),
                v.tvWpmc.text.toString(),sjr,sjrid
            ))
        }
    }

    override fun initData() {
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.data
        dialogPerson = ListDialog(this, "人员选择", listPerson, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvRy.text  = listPerson[position]
                val res=listPerson[position].split("/")
                sjrid = res[0]
                sjr = res[1]
            }
        })
    }
    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions(requestCode:Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_TITLE, "扫码")
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_IS_CONTINUOUS, com.eohi.hx.utils.Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                requestCode,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), com.eohi.hx.utils.Constant.RC_CAMERA, *perms
            )
        }
    }
    override fun initVM() {
        vm.detailmodel.observe(this) { it ->
            if (it.data.list.size > 0) {
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGg.text = it.data.list[0].GGMS
                v.tvWph.text = it.data.list[0].WPH
                v.tvScddh.text = it.data.list[0].SAPDDH
                v.tvLzkh.text = it.data.list[0].LZKKH
                v.tvRwdh.text =  it.data.list[0].RWBH
                v.tvBgjlslh.text = it.data.list[0].BGJYID.toString()
                v.tvBgsl.text = it.data.list[0].BGSL.toString()
                v.tvGltmh.text = it.data.list[0].GLTMH
            }else{
                v.tvWpmc.text = ""
                v.tvGg.text =""
                v.tvWph.text = ""
                v.tvLzkh.text = ""
            }
        }

        vm.personlist.observe(this, Observer {
            listPerson.clear()
            it.onEach {
                listPerson.add(it.ygbh + "/" + it.ygxm)
            }
            dialogPerson.show()
            dialogPerson.refreshList(listPerson)
        })

        vm.response.observe(this){
            showShortToast("提交成功")
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val result = data.getStringExtra(Intents.Scan.RESULT).trim { it <= ' ' }
            when (requestCode) {
                1001->{
                    v.tvTmh.setText(result)
                    vm.getDetailTMH(companyNo,result)
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
                v.tvTmh.setText(result)
                vm.getDetailTMH(companyNo,result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }


}