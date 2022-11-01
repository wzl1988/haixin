package com.eohi.hx.ui.main.agv.abnormal

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalListBean
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalPostBean
import com.eohi.hx.ui.main.agvmodel.AgvFailTaskModel
import com.eohi.hx.ui.main.agvmodel.TaskCancelModel
import com.eohi.hx.ui.work.model.SubmitResult

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/9/18 14:14
 */
class AgvAbnormalViewModel : BaseViewModel() {

    var iteminfo = MutableLiveData<ArrayList<AgvFailTaskModel>>()
    var agvresult = MutableLiveData<ArrayList<SubmitResult>>()
    var agvAbnormalList = MutableLiveData<ArrayList<AgvAbnormalListBean>>()
    var dealResult = MutableLiveData<ArrayList<SubmitResult>>()
    var cancelResult = MutableLiveData<ArrayList<SubmitResult>>()

    fun cancel(rwid:String,userid:String) {
        launchList({httpUtil.cancel(rwid,userid)},cancelResult)
    }

    fun dealAgvAbnormal(agvAbnormalPostBean: AgvAbnormalPostBean) {
        launchList({ httpUtil.dealAgvAbnormal(agvAbnormalPostBean) }, dealResult)
    }

    fun getAgvAbnormalList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getAgvAbnormalList(hashMap) }, agvAbnormalList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getAgvAbnormalAllList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getAgvAbnormalAllList(hashMap) }, agvAbnormalList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getFialTask(tmh: String) {
        launchList({ httpUtil.getFailAgvTask(tmh) }, iteminfo, true)
    }

    fun subMit(model: TaskCancelModel) {
        launchList({ httpUtil.submitCancelTask(model) }, agvresult, true)
    }

}