package com.eohi.hx.ui.work.tooling.onlineoffline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentToolOfflineBinding
import com.eohi.hx.ui.work.tooling.adapter.ToolOfflineAdapter
import com.eohi.hx.ui.work.tooling.model.ToolListModel
import com.eohi.hx.ui.work.tooling.model.ToolOfflineSubmitmodel
import com.eohi.hx.ui.work.tooling.viewmodel.ToolOnlineViewModel
import com.eohi.hx.utils.ToastUtil

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/8/3 10:13
 */
class ToolOfflineFragment :BaseFragment<ToolOnlineViewModel,FragmentToolOfflineBinding>(){
    private lateinit var adapter: ToolOfflineAdapter
    private lateinit var list: ArrayList<ToolListModel>
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.toollist.observe(this, Observer {
            (activity as ToolOnlineOfflineActivity).sbbh = vm.sbbh
            if(it.isNotEmpty()){
                list.clear()
                list.addAll(it)
                adapter.notifyDataSetChanged()
            }else{
                ToastUtil.showToast(mContext,"工装列表为空！")
            }
        })

        vm.offlineResult.observe(this, Observer {
            ToastUtil.showToast(mContext,"工装下线成功！")
            vm.getToolListBySbbh(vm.sbbh)
        })
    }

    override fun initView() {
    }

    override fun initClick() {
    }

    override fun initData() {
        list= ArrayList()
        adapter = ToolOfflineAdapter(mContext,list)
        adapter.setOfflineClick {
            vm.offlineSubmit(ToolOfflineSubmitmodel(list[it].GZBH,
                vm.sbbh))
        }
        v.rc.let {
            it.layoutManager = LinearLayoutManager(mContext)
            it.adapter = adapter
        }
    }

    override fun lazyLoadData() {

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
                vm.getToolList(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity?.unregisterReceiver(scanReceiver)
        super.onPause()
    }


}