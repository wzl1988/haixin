package com.eohi.hx.ui.work.purchasein.retreat

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.eohi.hx.App
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentPurchaseCodeBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.ui.main.model.KwModel
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import java.util.ArrayList

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/24 17:46
 */
class PurchaseByCodeFragment:BaseFragment<PurchaseByCodeViewModel, FragmentPurchaseCodeBinding>() {
    private var username by Preference("username", "")
    private var ishardware  = 0//是否是五金
    private var warehouseInfolist: ArrayList<WarehouseInfo>?=null
    private var cklist: ArrayList<String?> = ArrayList()
    private var mkwList: List<KwModel>? =null
    private var kwlist: ArrayList<String?> = ArrayList()
    var ckh:String?= null
    var yym:String=""
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

        vm.reasonlist.observe(this, Observer {
            if(it.isNotEmpty()){
                var yylist = ArrayList<String>()
                var yymlist = ArrayList<String>()
                for(i in it.indices){
                    yylist.add(it[i].YYSM)
                    yymlist.add(it[i].YYM)
                }
                dialogYY(yylist.toTypedArray(),yymlist.toTypedArray())
            }
        })

        vm.itemlist.observe(this, Observer {
            if(it.isNotEmpty()){
                v.tvWph.text = it[0].wph
                v.tvWpmc.text = it[0].wpmc
                v.tvGg.text = it[0].gg
                v.etZysl.text = Editable.Factory.getInstance().newEditable(it[0].sl.toString())
                v.etFzsl.setText(it[0].fzsl.toString())
                val item = ItemInfo()
                item.cktype = ishardware //仓库类型
                item.tmh = it[0].tmh
                item.FSL =it[0].fzsl.toString()
                item.GGMS =it[0].gg
                item.WPMC =it[0].wpmc
                item.wph =it[0].wph
                item.ZSL =it[0].sl.toString()
                item.ckh = v.etCk.text.toString()
                item.kwh = v.etKw.text.toString()
                item.yym =yym
                App.post(EventMessage(EventCode.DATA,"","",0,item))
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
        v.etFzsl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if(v.etTmh.text.isNotEmpty()){
                    App.post(EventMessage(EventCode.FSLREFRESH,v.etTmh.text.toString(), s.toString(),0,null))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }


        })


    }

    override fun initClick() {
        v.btnCk.setOnClickListener {
            vm.getCklist()
        }

        v.btnKw.setOnClickListener {
            if(null != ckh){
                if(kwlist.size>0){
                    dialogkwList()
                }else{
                    vm.getKwlist(ckh)
                }
            }
        }

        v.etTkyy.setOnClickListener{
            vm.getTkyym()
        }
    }

    override fun initData() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data
    }

    private fun dialogYY(items:Array<String>,yymlist:Array<String>){
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etTkyy.text = Editable.Factory.getInstance().newEditable(items[which])
            yym =yymlist[which]
        }
        builder.create().show()
    }

    private fun dialogList() {
        if (cklist.size == 0) return
        var  items = cklist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etCk.text = warehouseInfolist!![which].CKH
            if (null != warehouseInfolist) {
                val model = warehouseInfolist!![which]
                ckh =model.CKH
                ishardware = if (model.SFAGVCK.equals("true")) {
                    2 } else { 1 }
                vm.getKwlist(model.CKH)
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
            v.etKw.setText(mkwList!![which].kwh)
        }
        builder.create().show()
    }


    override fun lazyLoadData() {

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
                    v.etKw.isFocused->{
                        v.etKw.setText(result)
                        v.etTmh.requestFocus()
                    }
                    v.etTmh.isFocused->{
                        v.etTmh.setText(result)
                        vm.getItemInfo(result,v.etCk.text.toString(),v.etKw.text.toString())
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