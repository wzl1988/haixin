package com.eohi.hx.ui.main

import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.main.agvmodel.AddTaskModel
import com.eohi.hx.ui.main.agvmodel.AgvStatusModel
import com.eohi.hx.ui.main.agvmodel.ProductionlineModel
import com.eohi.hx.ui.main.model.PointModel
import com.eohi.hx.ui.main.model.PointReleaseModel
import com.eohi.hx.ui.main.model.SavePointInfo
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.SubmitResult
import com.eohi.hx.utils.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/10 16:01
 */

class AgvViewModel : BaseViewModel() {
    var agvresult = MutableLiveData<BaseResModel<SubmitResult>>()
    var agvpoint = MutableLiveData<BaseResModel<SubmitResult>>()
    var linelist = MutableLiveData<ArrayList<ProductionlineModel>>()

    var linelistrk = MutableLiveData<ArrayList<ProductionlineModel>>()

    var bhglinelist = MutableLiveData<ArrayList<ProductionlineModel>>()

    var setagvresult = MutableLiveData<ArrayList<SubmitResult>>()
    var agvstatus = MutableLiveData<ArrayList<AgvStatusModel>>()
    //起点自动分配
    fun addtaskAutoStart(endpoint: String, startRegionid: String) {
        val map = ArrayMap<String, String>()
        map["method"] = "AgvPointInfo_GetPoint"
        map["RegionId"] = startRegionid
        map["TakeUpStatus"] = "1" //1起点0终点
        showLoading()
        val pointmodel = PointModel()
        pointmodel.takeUpStatus = 0
        pointmodel.locationPoint = endpoint
        viewModelScope.launch {
            try {
                val basepoint = withContext(Dispatchers.IO) { httpUtil.checkPointStuas(pointmodel) }
                if (basepoint.code == 1000) {
                    val agvstart = withContext(Dispatchers.IO) { httpUtil.getAgvStart(map) }
                    if (agvstart.code == 1000) {
                        val taskModel = AddTaskModel()
                        taskModel.areaId = agvstart.data?.list!![0].areaId
                        taskModel.fromSystem = "03"
                        taskModel.modelProcessCode = "kaiwang"
                        taskModel.priority = 6
                        val orderID = "AGV" + UUID.randomUUID()
                        taskModel.orderId = orderID
                        val bean = AddTaskModel.TaskOrderDetailBean()
                        bean.taskPath = agvstart.data?.list!![0].locationPoint + "," + endpoint
                        val list: ArrayList<AddTaskModel.TaskOrderDetailBean> =
                            ArrayList<AddTaskModel.TaskOrderDetailBean>()
                        list.add(bean)
                        taskModel.taskOrderDetail = list
                        //大华添加任务
                        val taskresult = withContext(Dispatchers.IO) { httpUtil.addTask(taskModel) }
                        //本地任务记录
                        val pointinfo = SavePointInfo()
                        pointinfo.areaId = agvstart.data?.list!![0].areaId
                        pointinfo.startRegionId = startRegionid
                        pointinfo.fromSystem = "03"
                        pointinfo.modelProcessCode = "kaiwang"
                        pointinfo.status = 4
                        val userid by Preference("userid", "")
                        val username by Preference("username", "");
                        pointinfo.createCode = userid
                        pointinfo.createUserName = username
                        pointinfo.orderId = orderID
                        pointinfo.startPoint = agvstart.data?.list!![0].locationPoint
                        pointinfo.endPoint = endpoint
                        //添加任务记录
                        withContext(Dispatchers.IO) { httpUtil.submitPointInfo(pointinfo) }
                        if (taskresult.code != 1000) {
                            showError(ErrorResult(taskresult.code, taskresult.desc, true))
                            //释放点位
                            val pointmodel = PointModel()
                            pointmodel.takeUpStatus = 0
                            pointmodel.locationPoint =
                                agvstart.data?.list!![0].locationPoint + "," + endpoint
                            withContext(Dispatchers.IO) { httpUtil.releasePoint(pointmodel) }
                        }
                    } else {
                        showError(ErrorResult(agvstart.code, agvstart.msg, true))
                    }
                } else {
                    showError(ErrorResult(basepoint.code, basepoint.msg, true))
                }

            } catch (e: Exception) {
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }


        }

    }

    //终点自动分配
    fun addTaskAutoEnd(startpoint: String, endRegionid: String) {
        //检查点位是否可用
        val pointmodel = PointModel()
        pointmodel.takeUpStatus = 1 //起点
        pointmodel.locationPoint = startpoint
        //自动获取终点
        val map = ArrayMap<String, String>()
        map["method"] = "AgvPointInfo_GetPoint"
        map["RegionId"] = endRegionid
        map["TakeUpStatus"] = "0" //1起点0终点
        showLoading()
        viewModelScope.launch {
            try {
                val basepoint = withContext(Dispatchers.IO) { httpUtil.checkPointStuas(pointmodel) }
                if (basepoint.code == 1000) {
                    val agvstart = withContext(Dispatchers.IO) { httpUtil.getAgvStart(map) }
                    if (agvstart.code == 1000) {

                        val taskModel = AddTaskModel()
                        taskModel.areaId = agvstart.data?.list!![0].areaId
                        taskModel.fromSystem = "03"
                        taskModel.modelProcessCode = "kaiwang"
                        taskModel.priority = 6
                        val orderID = "AGV" + UUID.randomUUID()
                        taskModel.orderId = orderID
                        val bean = AddTaskModel.TaskOrderDetailBean()
                        bean.taskPath = startpoint + "," + agvstart.data?.list!![0].locationPoint
                        val list: ArrayList<AddTaskModel.TaskOrderDetailBean> =
                            ArrayList<AddTaskModel.TaskOrderDetailBean>()
                        list.add(bean)
                        taskModel.taskOrderDetail = list
                        //大华添加任务
                        val taskresult = withContext(Dispatchers.IO) { httpUtil.addTask(taskModel) }

                        //本地任务记录
                        val pointinfo = SavePointInfo()
                        pointinfo.areaId = agvstart.data?.list!![0].areaId
                        pointinfo.endRegionId = endRegionid
                        pointinfo.fromSystem = "03"
                        pointinfo.modelProcessCode = "kaiwang"
                        pointinfo.status = 4
                        val userid by Preference("userid", "")
                        val username by Preference("username", "");
                        pointinfo.createCode = userid
                        pointinfo.createUserName = username
                        pointinfo.orderId = orderID
                        pointinfo.startPoint = startpoint
                        pointinfo.endPoint = agvstart.data?.list!![0].locationPoint
                        //添加任务记录
                        withContext(Dispatchers.IO) { httpUtil.submitPointInfo(pointinfo) }
                        if (taskresult.code != 1000) {
                            showError(ErrorResult(taskresult.code, "添加agv任务失败！", true))
                            //释放点位
                            val pointmodel = PointModel()
                            pointmodel.takeUpStatus = 0
                            pointmodel.locationPoint =
                                startpoint + "," + agvstart.data?.list!![0].locationPoint
                            withContext(Dispatchers.IO) { httpUtil.releasePoint(pointmodel) }
                        }

                    } else {
                        showError(ErrorResult(agvstart.code, agvstart.msg, true))
                    }

                } else {
                    showError(ErrorResult(basepoint.code, basepoint.msg, true))
                }

            } catch (e: Exception) {
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }

        }
    }


    fun addtaskStarttoEnd(
        startpoint: String,
        endpoint: String,
        startRegionid: String,
        endRegionid: String
    ) {

        //检查点位是否可用
        val pointmodel = PointModel()
        pointmodel.takeUpStatus = 1 //起点
        pointmodel.locationPoint = startpoint
        val endpointmodel = PointModel()
        endpointmodel.takeUpStatus = 0 //终点
        endpointmodel.locationPoint = (endpoint)
        showLoading()
        viewModelScope.launch {
            try {
                val basepoint = withContext(Dispatchers.IO) { httpUtil.checkPointStuas(pointmodel) }
                if (basepoint.code == 1000) {
                    val end =
                        withContext(Dispatchers.IO) { httpUtil.checkPointStuas(endpointmodel) }
                    if (1000 == end.code) {
                        val taskModel = AddTaskModel()
                        taskModel.areaId = 1
                        taskModel.fromSystem = "03"
                        taskModel.modelProcessCode = "kaiwang"
                        taskModel.priority = 6
                        val orderID = "AGV" + UUID.randomUUID()
                        taskModel.orderId = orderID
                        val bean = AddTaskModel.TaskOrderDetailBean()
                        bean.taskPath = "$startpoint,$endpoint"
                        val list: ArrayList<AddTaskModel.TaskOrderDetailBean> =
                            ArrayList<AddTaskModel.TaskOrderDetailBean>()
                        list.add(bean)
                        taskModel.taskOrderDetail = list
                        //大华添加任务
                        val taskresult = withContext(Dispatchers.IO) { httpUtil.addTask(taskModel) }
                        //本地任务记录
                        val pointinfo = SavePointInfo()
                        pointinfo.areaId = 1
                        pointinfo.startRegionId = startRegionid
                        pointinfo.endRegionId = endRegionid
                        pointinfo.fromSystem = "03"
                        pointinfo.modelProcessCode = "kaiwang"
                        pointinfo.status = 4
                        val userid by Preference("userid", "")
                        val username by Preference("username", "");
                        pointinfo.createCode = userid
                        pointinfo.createUserName = username
                        pointinfo.orderId = orderID
                        pointinfo.startPoint = startpoint
                        pointinfo.endPoint = endpoint

                        //添加任务记录
                        withContext(Dispatchers.IO) { httpUtil.submitPointInfo(pointinfo) }
                        if (taskresult.code != 1000) {
                            showError(ErrorResult(taskresult.code, "添加agv任务失败！", true))
                            //释放点位
                            val pointmodel = PointModel()
                            pointmodel.takeUpStatus = 0
                            pointmodel.locationPoint = "$startpoint,$endpointmodel"
                            withContext(Dispatchers.IO) { httpUtil.releasePoint(pointmodel) }
                        }

                    } else {
                        showError(ErrorResult(end.code, end.msg, true))
                    }
                } else {
                    showError(ErrorResult(basepoint.code, basepoint.msg, true))
                }

            } catch (e: Exception) {
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }

        }

    }


    //agv任务提交
    fun agvTaskadd(agvmodel: AgvSubmitModel) {
        viewModelScope.launch {
            agvresult.value = withContext(Dispatchers.IO) { httpUtil.addAGV(agvmodel) }
        }
    }


    //获取产线列表
    fun getLinelist(gsh: String) {
        launchList({ httpUtil.getProlineList(gsh) }, linelist, true)
    }

    //备货区空货 架至产线
    fun getLinelistRk(gsh: String) {
        launchList({ httpUtil.getProlineList(gsh) }, linelistrk, true)
    }


    //不合格品-产线
    fun getLinebhg(gsh: String) {
        launchList({ httpUtil.getProlineList(gsh) }, bhglinelist, true)
    }

    //站点释放
    fun releasePoint(model: PointReleaseModel) {
        viewModelScope.launch {
            agvpoint.value = withContext(Dispatchers.IO) { httpUtil.releasePoint(model) }
        }
    }

    //AGV开关
    fun setAgv(zt:Int){
        launchList({ httpUtil.setAgv(zt) }, setagvresult, true)
    }
    //AGV状态
    fun getAgvStatus(){
        launchList({ httpUtil.getAgvStatus() }, agvstatus, true)
    }


}