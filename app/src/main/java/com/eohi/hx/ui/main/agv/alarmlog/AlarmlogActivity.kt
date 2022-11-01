package com.eohi.hx.ui.main.agv.alarmlog

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.BitmapDrawable
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityAlarmlogBinding
import com.eohi.hx.ui.main.model.AlarmReadSub
import com.eohi.hx.ui.main.model.AlarmlogModel
import com.eohi.hx.view.PopAlarmLogSearch
import com.eohi.hx.widget.clicks
import kotlinx.android.synthetic.main.activity_alarmlog.*
import java.util.ArrayList

class AlarmlogActivity : BaseActivity<AlarmlogViewModel,ActivityAlarmlogBinding>() {
    private var starttime:String =""
    private var endtime:String =""
    private var code:String =""
    private var isread:String =""
    private var id:String =""
    private var page =1
    var nowIndex = 0
    private lateinit var pop :PopAlarmLogSearch
    private lateinit var agvAbnormalList: ArrayList<AlarmlogModel>
    private lateinit var adapter: AlarmlogAdapter
    override fun isNeedEventBus(): Boolean {
       return false
    }

    override fun initView() {
    }

    override fun initClick() {
        pop= PopAlarmLogSearch(this,object:PopAlarmLogSearch.MyListener{
            override fun onSearckClick(start: String, end: String, c: String,isrd:String,userid:String) {
                starttime = start
                endtime = end
                code = c
                isread = isrd
                id = userid
                vm.getAlarmlogList(start,end,c,isrd,userid,pageindex = 1)
            }
        })
        pop.setBackgroundDrawable( BitmapDrawable())
        pop.isOutsideTouchable = true
        iv_search.clicks {
            if(!pop.isShowing){
                pop.isFocusable = true
                pop.width = ViewGroup.LayoutParams.MATCH_PARENT
                pop.height = ViewGroup.LayoutParams.WRAP_CONTENT
                pop.showAsDropDown(iv_search)
//                pop.showAtLocation(constraintLayout, Gravity.TOP, 0, 0)
            }
        }
        iv_back.clicks { finish() }

    }

    override fun initData() {
        agvAbnormalList = ArrayList()
        adapter = AlarmlogAdapter(this,agvAbnormalList)
        v.rc.layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        v.rc.addItemDecoration(dividerItemDecoration)
        v.rc.adapter = adapter

        adapter.setReadClick {
            nowIndex = it
            vm.read(AlarmReadSub(agvAbnormalList[it].DjLsh))
        }

        v.refreshLayout.setOnRefreshListener {
            page = 1
            getlist()
        }
        v.refreshLayout.setOnLoadMoreListener {
            page++
            getlist()
        }

    }

    private fun getlist(){
        vm.getAlarmlogList(starttime,endtime,code,isread,id,pageindex = page)
    }


    override fun initVM() {
        vm.agvAbnormalList.observe(this, Observer {
            v.refreshLayout.finishRefresh()
            v.refreshLayout.finishLoadMore()
            if (page == 1) agvAbnormalList.clear()
            agvAbnormalList.addAll(it)
            adapter.notifyDataSetChanged()
        })
        vm.result.observe(this, Observer {
            agvAbnormalList[nowIndex].alarmReadFlag =1
            adapter.notifyDataSetChanged()
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
                if(pop.isShowing)
                pop.setCode(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}