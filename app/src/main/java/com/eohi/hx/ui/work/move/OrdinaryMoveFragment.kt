package com.eohi.hx.ui.work.move

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentOrdinaryMoveBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.ui.main.model.KwModel
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.util.ArrayList


/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/19 15:07
 */
class OrdinaryMoveFragment :BaseFragment<OrdinaryMoveViewModel,FragmentOrdinaryMoveBinding>() {
    private var username by Preference("username", "")
    private var ishardware  = 0//是否是五金
    private var warehouseInfolist: ArrayList<WarehouseInfo>?=null
    private var cklist: ArrayList<String?> = ArrayList()
    private var mkwList: List<KwModel>? =null
    private var kwlist: ArrayList<String?> = ArrayList()

    var ckh:String?= null
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.whlist.observe(this, Observer {
            warehouseInfolist = it
            cklist.clear()
            for (i in warehouseInfolist!!.indices){
                if(null !=warehouseInfolist!![i].CKMC )
                    cklist.add(warehouseInfolist!![i].CKMC)
            }
            dialogList(v.etStartck,false)
        })

        vm.kwlist.observe(this, Observer {
            mkwList =  it
            kwlist.clear()
            for (i in mkwList!!.indices){
                if(null !=mkwList!![i].kwmc )
                    kwlist.add(mkwList!![i].kwmc)
            }
//            dialogkwList(v.etStartkw)
        })

        vm.endcklist.observe(this, Observer {
            warehouseInfolist = it
            cklist.clear()
            for (i in warehouseInfolist!!.indices){
                if(null !=warehouseInfolist!![i].CKMC )
                    cklist.add(warehouseInfolist!![i].CKMC)
            }
            dialogList(v.etEndck,true)
        })
        vm.endkwlist.observe(this, Observer {
            mkwList =  it
            kwlist.clear()
            for (i in mkwList!!.indices){
                if(null !=mkwList!![i].kwmc )
                    kwlist.add(mkwList!![i].kwmc)
            }
//            dialogkwList(v.etEndkw)
        })


        vm.itemlist.observe(this, Observer {
            if(it.isNotEmpty()){
                v.tvWph.text = it[0].wph
                v.tvWpmc.text = it[0].wpmc
                v.tvGg.text = it[0].gg
                v.etZysl.text =Editable.Factory.getInstance().newEditable(it[0].sl.toString())

                val item = ItemInfo()
                item.tmh = it[0].tmh
                item.FSL =it[0].fzsl.toString()
                item.GGMS =it[0].gg
                item.WPMC =it[0].wpmc
                item.wph =it[0].wph
                item.ZSL =it[0].sl.toString()
                item.fckh = v.etStartck.text.toString()
                item.fkwh = v.etStartkw.text.toString()
                item.ckh = v.etEndck.text.toString()
                item.kwh = v.etEndkw.text.toString()
                App.post(EventMessage(EventCode.DATA,"","",0,item))
            }else{
                ToastUtil.showToast(mContext,"数据为空！")
            }
        })

    }

    override fun initView() {
        v.etZysl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(v.etTmh.text.isNotEmpty()){
                    App.post(EventMessage(EventCode.REFRESH,v.etTmh.text.toString(), s.toString(),0,null))
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }
    @SuppressLint("ClickableViewAccessibility")
    override fun initClick() {
        v.btnStartck.setOnClickListener {
            vm.getCklist()
        }
        v.btnEndck.setOnClickListener {
            vm.getEndcklist()
        }

        v.btnStartkw.setOnClickListener {
            if(null != ckh){
                if(kwlist.size>0){
                    dialogkwList( v.etStartkw)
                }else{
                    vm.getKwlist(ckh)
                }
            }
        }

        v.btnEndkw.setOnClickListener {
            if(v.etEndck.text.isNotEmpty()){
                if(kwlist.size>0){
                    dialogkwList( v.etEndkw)
                }else{
                    vm.getEndkwlist(v.etEndck.text.toString())
                }
            }
        }

        v.etStartkw.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etStartkw.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etStartkw.width
                    - v.etStartkw.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(Constant.REQUEST_CODE_SCAN)
                }
                return false
            }
        })
        v.etEndkw.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etEndkw.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etEndkw.width
                    - v.etEndkw.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(Constant.REQUEST_CODE_SCAN_02)
                }
                return false
            }
        })
        v.etTmh.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etTmh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etTmh.width
                    - v.etTmh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(Constant.REQUEST_CODE_SCAN_03)
                }
                return false
            }
        })

    }


    private fun dialogList(tv:TextView,isend:Boolean) {
        if (cklist.size == 0) return
        var  items = cklist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            tv.text = warehouseInfolist!![which].CKH
            if (null != warehouseInfolist) {
                val model = warehouseInfolist!![which]
                ckh =model.CKH
                ishardware = if (model.SFAGVCK.equals("true")) {
                    2 } else { 1 }
                if(isend){
                    vm.getEndkwlist(model.CKH)
                }else{
                    vm.getKwlist(model.CKH)
                }

            }
        }
        builder.create().show()
    }


    private fun dialogkwList(tv:TextView) {
        if (kwlist.size == 0) return
        var  items = kwlist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            tv.text = mkwList!![which].kwh
        }
        builder.create().show()
    }


    override fun initData() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data
    }

    override fun lazyLoadData() {
    }

    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions(code: Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(
                    requireActivity(),
                    R.anim.`in`,
                    R.anim.out
                )
            val intent = Intent(requireActivity(), CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                requireActivity(),
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
        requireActivity().registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }
    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION && isVisibleToUser) {
                if(v.etStartck.text.isEmpty()){
                    ToastUtil.showToast(mContext,"仓库号不能为空")
                    return
                }

                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                when{
                    v.etStartkw.isFocused->{
                        v.etStartkw.setText(result)
                        v.etEndkw.requestFocus()
                    }
                    v.etEndkw.isFocused->{
                        v.etEndkw.setText(result)
                        v.etTmh.requestFocus()
                    }
                    v.etTmh.isFocused->{
                        v.etTmh.text = Editable.Factory.getInstance().newEditable(result)
                        vm.getItemInfo(result,v.etStartck.text.toString(),v.etStartkw.text.toString())
                    }

                }


            }
        }
    }

    override fun onPause() {
        //取消广播注册
        requireActivity().unregisterReceiver(scanReceiver)
        super.onPause()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            val result = data?.getStringExtra(Intents.Scan.RESULT).toString().trim { it <= ' ' }
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN->{
                    v.etStartkw.setText(result)
                    v.etEndkw.requestFocus()
                }
                Constant.REQUEST_CODE_SCAN_02->{
                    v.etEndkw.setText(result)
                    v.etTmh.requestFocus()
                }
                Constant.REQUEST_CODE_SCAN_03->{
                    v.etTmh.text = Editable.Factory.getInstance().newEditable(result)
                    vm.getItemInfo(result,v.etStartck.text.toString(),v.etStartkw.text.toString())
                }
            }
            }

    }

}