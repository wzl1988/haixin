package com.eohi.hx.ui.main.agv.alarmlog

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.main.model.AlarmReadSub
import com.eohi.hx.ui.main.model.AlarmlogModel
import com.eohi.hx.ui.work.model.SubmitResult

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/2/21 15:00
 */
class AlarmlogViewModel:BaseViewModel() {
    var agvAbnormalList = MutableLiveData<ArrayList<AlarmlogModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()
    fun getAlarmlogList(begintime: String, endtime: String,code: String, isread: String, userid:String,pagesize: Int=10,pageindex:Int){
        launchList({httpUtil.getAlarmLogList(begintime,endtime,code,isread,userid,pagesize,pageindex)},agvAbnormalList,true)
    }

    fun read(body: AlarmReadSub){
        launchList({httpUtil.subReadLog(body)},result,true)
    }

}