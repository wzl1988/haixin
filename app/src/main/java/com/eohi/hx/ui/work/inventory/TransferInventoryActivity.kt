package com.eohi.hx.ui.work.inventory

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityTransferInventoryBinding
import com.eohi.hx.ui.work.adapter.SimpleDecoration
import com.eohi.hx.ui.work.adapter.TransferInventoryAdapter
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.TransferKwModel
import com.eohi.hx.ui.work.model.TransferKwWpModel
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.widget.clicks

/**
 *@author: YH
 *@date: 2021/12/7
 *@desc: 调库存栈板 042
 */
class TransferInventoryActivity :
    BaseActivity<TransferInventoryViewModel, ActivityTransferInventoryBinding>() {

    private lateinit var tmh: String
    private lateinit var qd: String
    private lateinit var zkw: String
    private lateinit var listKw: ArrayList<TransferKwModel>
    private lateinit var listKwmc: ArrayList<String>
    private lateinit var listKwWp: ArrayList<TransferKwWpModel>
    private lateinit var adapter: TransferInventoryAdapter

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        v.mRecyclerView.layoutManager = LinearLayoutManager(this)
        v.mRecyclerView.adapter = adapter
        v.mRecyclerView.addItemDecoration(SimpleDecoration(this))
    }

    override fun initClick() {
        v.btnPost clicks {
            when {
                TextUtils.isEmpty(v.etTmh.text.toString()) -> {
                    showShortToast("请先扫描条码号")
                }
                TextUtils.isEmpty(v.etZkw.text.toString()) -> {
                    showShortToast("请扫描至库位")
                }
                else -> {
                    val agvSubmitModel =
                        AgvSubmitModel(companyNo, accout, "042", qd, zkw, "调库存栈板", "", 0, "")
                    vm.postAgv(agvSubmitModel)
                }
            }
        }
    }

    override fun initData() {
        listKw = ArrayList()
        listKwWp = ArrayList()
        listKwmc = ArrayList()
        adapter = TransferInventoryAdapter(this, listKwWp)
        v.spKwh.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                qd = listKw[position].kwh
                v.tvCk.text = listKw[position].ckmc
                vm.getTransferKwWp(listKw[position].kwh)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    override fun initVM() {
        vm.transferWp.observe(this) {
            if(it.isNotEmpty()){
                v.tvWph.text = it[0].cgwph
                vm.getTransferKw(it[0].cgwph)
            }
        }
        vm.transferKw.observe(this) { it ->
            listKw.clear()
            listKw.addAll(it)
            listKwmc.clear()
            listKw.forEach {
                listKwmc.add(it.kwh+"+"+it.sl)
            }
            val adapter = ArrayAdapter(
                mContext,
                android.R.layout.simple_spinner_item,
                listKwmc
            )
            v.spKwh.adapter = adapter
        }
        vm.transferKwWp.observe(this) {
            if (listKwWp.size > 0) {
                listKwWp.clear()
            }
            listKwWp.addAll(it)
            v.tvSl.text = listKwWp.size.toString()
            adapter.notifyDataSetChanged()
        }
        vm.postResult.observe(this) {
            showShortToast(it.msg!!)
            finish()
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
                when {
                    v.etTmh.isFocused -> {
                        tmh = result
                        v.etTmh.setText(result)
                        vm.getTransferWp(tmh)
                    }
                    v.etZkw.isFocused -> {
                        zkw = result
                        v.etZkw.setText(result)
                    }
                }
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}