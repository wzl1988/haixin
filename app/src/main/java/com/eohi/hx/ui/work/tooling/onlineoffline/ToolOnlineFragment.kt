package com.eohi.hx.ui.work.tooling.onlineoffline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentToolOnlineBinding
import com.eohi.hx.ui.work.tooling.adapter.ToolOnLineAdapter
import com.eohi.hx.ui.work.tooling.model.OnLineItem
import com.eohi.hx.ui.work.tooling.model.ToolOnlineSubmitModel
import com.eohi.hx.ui.work.tooling.model.ToolinfoModel
import com.eohi.hx.ui.work.tooling.viewmodel.ToolOnlineViewModel
import com.eohi.hx.utils.ToastUtil

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/8/3 10:13
 */
class ToolOnlineFragment :BaseFragment<ToolOnlineViewModel,FragmentToolOnlineBinding>(){
    private lateinit var adapter: ToolOnLineAdapter
    private lateinit var listdata: ArrayList<ToolinfoModel>
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.gzxx.observe(this, Observer {
            if(it.isNotEmpty()){
                var b = true
                for (i in listdata.indices){
                    if(listdata[i].gzbm == it[0].gzbm){
                        b = false
                        break
                    }
                }
                if(b){
                    listdata.addAll(it)
                    adapter.notifyDataSetChanged()
                }else{
                    ToastUtil.showToast(mContext,"工装已存在！")
                }

            }
        })
        vm.onLineResult.observe(this, Observer {
            listdata.clear()
            adapter.notifyDataSetChanged()
            ToastUtil.showToast(mContext,"提交成功！")
        })
    }

    override fun initView() {
    }

    override fun initClick() {

    }

    override fun initData() {
        listdata = ArrayList()
        adapter  = ToolOnLineAdapter(mContext,listdata)
        v.rc.let {
            it.layoutManager = LinearLayoutManager(mContext)
            it.adapter = adapter
        }
    }

    override fun lazyLoadData() {
    }

    fun submit(){
        if(listdata.isNotEmpty()){
            var items= ArrayList<OnLineItem>()
            listdata.forEach {
                items.add(OnLineItem(it.gzbm))
            }
            vm.onLineSubmit( ToolOnlineSubmitModel(items,(activity as ToolOnlineOfflineActivity).sbbh,
                accout))
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
            if (intent.action == SCANACTION && isVisibleToUser) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.tvGzbh.setText(result)
                vm.getGzxx(result, companyNo)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity?.unregisterReceiver(scanReceiver)
        super.onPause()
    }

}