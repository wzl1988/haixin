package com.eohi.hx.ui.work.purchasein

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.text.Editable
import android.text.TextWatcher
import android.util.ArrayMap
import android.view.View
import androidx.lifecycle.Observer
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentInstorageBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.KwModel
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.utils.DateUtil.data
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import kotlin.collections.ArrayList

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/12 9:54
 */
class InstorageFragment :BaseFragment<InstoragerFmViewModel, FragmentInstorageBinding>() {
    private var username by Preference("username", "")
    private var ishardware  = 0//是否是五金
    private var warehouseInfolist: ArrayList<WarehouseInfo> ?=null
    private var cklist: ArrayList<String?> = ArrayList()
    private var mkwList: ArrayList<KwModel>? =null
    private var kwlist:ArrayList<String?> = ArrayList()
    var locationId:String? =null
    private var mediaplayer: MediaPlayer? = null

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
            dialogList()
        })

        vm.kwlist.observe(this, Observer {
            mkwList =  it
            kwlist.clear()
            for (i in mkwList!!.indices){
                if(null !=mkwList!![i].kwmc )
                    kwlist.add(mkwList!![i].kwmc)
            }
//            dialogkwList()
        })

        vm.iteminfo.observe(this, Observer {
            if(!it.isNullOrEmpty()){
                val item=  it[0]
                v.tvCgdh.text = item.CGDDH
                v.tvWph.text = item.wph
                v.tvWpmc.text = item.WPMC
                v.tvGg.text = item.GGMS
                v.tvDw.text = item.ZDW
                v.tvNumber.setText(item.ZSL)
                item.tmh = v.etTmh.text.toString()
                item.cktype = ishardware //仓库类型
                v.etFzsl.text = item.fsl.toString()
                v.tvFzdw.text = item.fdw
                v.tvPh.text = item.ph
                if(v.etFzsl.text.isEmpty()){
                    item.FSL = "0"
                }else{
                    item.FSL = v.etFzsl.text.toString()
                }

                if(0 == ishardware)
                {
                    ToastUtil.showToast(mContext,"请先选择仓库")
                    return@Observer
                }
                item.ckh = v.etCkh.text.toString()
                item.kwh = v.etKwh.text.toString()
                if(v.etTmh.text.isNotEmpty()){
                    App.post(EventMessage(EventCode.DATA,"","",0,item))
                }

            }else{
                mediaplayer = MediaPlayer.create(mContext, R.raw.beep)
                mediaplayer!!.isLooping = false
                mediaplayer!!.start()
            }
        })
        vm.errorData.observe(this, Observer {
            ToastUtil.showToast(mContext,it.errMsg)
            mediaplayer = MediaPlayer.create(mContext, R.raw.beep)
            mediaplayer!!.isLooping = false
            mediaplayer!!.start()
        })
    }

    override fun initView() {
        v.tvUser.text = username
        v.tvDate.text = data
        v.etGdh.visibility =View.GONE
        v.tvGdh.visibility =View.GONE

        v.etCkh.setOnClickListener(View.OnClickListener {
            if (warehouseInfolist !=null && warehouseInfolist!!.isNotEmpty()) {
                dialogList()
            } else {
                mkwList?.clear()
               vm.getCklist()
            }
        })
        v.etKwh.setOnClickListener {
            if (mkwList !=null && mkwList!!.isNotEmpty()) {
                dialogkwList()
            }
        }





    }


    private fun dialogList() {
        if (cklist.size == 0) return
        var  items = cklist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etCkh.text = warehouseInfolist!![which].CKH
            if (null != warehouseInfolist) {
                val model = warehouseInfolist!![which]
                //是AGV
                if (model.SFAGVCK.equals("true")) {
                    ishardware = 2
                    v.etKwh.setText("自动分配库位")
                } else {
                    v.etKwh.setText("")
                    ishardware = 1
                    vm.getKwlist(model.CKH)
                }
            }
        }
        builder.create().show()
    }


    private fun dialogkwList() {
        if (kwlist.size == 0) return
        var  items = kwlist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etKwh.setText(mkwList!![which].kwh)
            if (null != mkwList) locationId = mkwList!![which].kwh
        }
        builder.create().show()
    }




    override fun initClick() {
    }

    override fun initData() {
        v.tvNumber.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if(v.etTmh.text.isNotEmpty()){
                    App.post(EventMessage(EventCode.REFRESH,v.etTmh.text.toString(), s.toString(),0,null))
                }
            }

        })
    }

    override fun lazyLoadData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mediaplayer?.stop()
            mediaplayer?.release() //释放资源
        } catch (e: Exception) {
        }
    }


    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        activity!!.registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }
    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION && isVisibleToUser) {
                if(0 == ishardware)
                {
                    ToastUtil.showToast(mContext,"请先选择仓库")
                    return
                }
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                when{
                    v.etTmh.isFocused->{
                        v.etTmh.setText(result)
                        val map = ArrayMap<String?, String?>()
                        map["Barcode"] = result
                        map["gsh"] = companyNo
                        vm.getItemInfo(map)
                    }
                    v.etKwh.isFocused->{
                        v.etKwh.setText(result)
                        v.etTmh.requestFocus()
                    }
                }


            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity!!.unregisterReceiver(scanReceiver)
        super.onPause()
    }



}