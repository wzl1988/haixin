package com.eohi.hx.ui.work.quality.rejects

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.ActivityRejectsListBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.agvmodel.ProductionlineModel
import com.eohi.hx.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.hx.ui.work.quality.rejects.model.RejectsListModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showAlertDialog
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.SpacesItemDecoration
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import java.util.ArrayList

class RejectsListActivity : BaseActivity<BaseViewModel,ActivityRejectsListBinding>(), EasyPermissions.PermissionCallbacks {
    var list = MutableLiveData<ArrayList<RejectsListModel>>()
    lateinit var adapter: RejectsItemAdapter
    lateinit var listdata :ArrayList<RejectsListModel>
    private var alldata = ArrayList<RejectsListModel>()
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.etScry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if( alldata.isEmpty() )
                    return
                listdata.clear()
                if(s.toString().isNotEmpty()){
                    for(i in alldata.indices){
                        try{
                        if(alldata[i]?.jgdybh?.contains(s.toString())||alldata[i]?.jgdymc?.contains(s.toString())){
                            if(v.etWph.text.isNotEmpty()){
                                if( alldata[i].cjbh?.contains(v.etWph.text.toString()) ||alldata[i].cjmc?.contains(v.etWph.text.toString()))
                                    listdata.add(alldata[i])
                            }else{
                                listdata.add(alldata[i])
                            }

                        }}catch (e:Exception){
                            continue
                        }
                    }
                }else{
                    listdata.addAll(alldata)
                }
                v.tvDjy.text = "当前记录数量："+listdata.size
                adapter.notifyDataSetChanged()
            }
        })
        v.etWph.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if( alldata.isEmpty() )
                    return
                listdata.clear()
                if(s.toString().isNotEmpty() ){
                    for(i in alldata.indices){
                        try{
                        if(alldata[i].cjbh!!.contains(s.toString()) ||alldata[i].cjmc!!.contains(s.toString())){
                            if(v.etScry.text.isNotEmpty()){
                                if( alldata[i].jgdymc?.contains(v.etScry.text.toString()) ||alldata[i].jgdybh?.contains(s.toString()))
                                    listdata.add(alldata[i])
                            }else{
                                listdata.add(alldata[i])
                            }
                        }}catch (e:Exception){
                            continue
                        }
                    }
                }else{
                    listdata.addAll(alldata)
                }
                v.tvDjy.text = "当前记录数量："+listdata.size
                adapter.notifyDataSetChanged()
            }
        })

        v.tvLzkbh.setOnTouchListener { _, event ->
            val drawable: Drawable = v.tvLzkbh.compoundDrawables[2] ?: return@setOnTouchListener false
            //如果右边没有图片，不再处理
            //如果不是按下事件，不再处理
            if (event.action != MotionEvent.ACTION_DOWN) {
                return@setOnTouchListener false;
            }
            if (event.x >  v.tvLzkbh.width
                -  v.tvLzkbh.paddingRight
                - drawable.intrinsicWidth
            ) {
                checkCameraPermissions()
            }else{
            }
            return@setOnTouchListener false
        }


    }

    override fun initData() {
        listdata = ArrayList()
        alldata=  ArrayList()
        adapter = RejectsItemAdapter(this,listdata)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 20, 20))
        vm.launchList({vm.httpUtil.getRejectsList(accout)},list,true, successCode = 200)
    }

    override fun initVM() {
        list.observe(this, Observer {
           if(it.isNotEmpty()){
               listdata.clear()
               alldata.clear()
               alldata.addAll(it)
               listdata.addAll(it)
               adapter.notifyDataSetChanged()
               v.tvDjy.text = "当前记录数量："+it.size
           }else{
               listdata.clear()
               alldata.clear()
               adapter.notifyDataSetChanged()
               v.tvDjy.text = "当前记录数量：0"
           }

        })
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
                v.tvLzkbh.setText(result)
                if( alldata.isEmpty() )
                    return
                listdata.clear()
                for(i in alldata.indices){
                    if(alldata[i].sclzkkh.contains(result)){
                        listdata.add(alldata[i])
                    }
                }
                v.tvDjy.text = "当前记录数量："+listdata.size
                adapter.notifyDataSetChanged()

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
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
               val result=data?.getStringExtra(Intents.Scan.RESULT)?.trim { it <= ' ' }!!
                v.tvLzkbh.setText(result)
                if( alldata.isEmpty() )
                    return
                listdata.clear()
                for(i in alldata.indices){
                    if(alldata[i].sclzkkh.contains(result)){
                        listdata.add(alldata[i])
                    }
                }
                v.tvDjy.text = "当前记录数量："+listdata.size
                adapter.notifyDataSetChanged()
            }
        }
    }

    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions() {
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
                1,
                optionsCompat.toBundle()
            )
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


    override fun handleEvent(msg: EventMessage) {
        super.handleEvent(msg)
        when(msg.code){
            EventCode.REFRESH->{
                vm.launchList({vm.httpUtil.getRejectsList(accout)},list,true, successCode = 200)
            }
        }
    }

}