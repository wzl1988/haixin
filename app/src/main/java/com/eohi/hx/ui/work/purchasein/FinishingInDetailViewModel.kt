package com.eohi.hx.ui.work.purchasein

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.work.model.AgvLocationType
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.InstorageModel
import com.eohi.hx.ui.work.model.SubmitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/13 10:25
 */
class FinishingInDetailViewModel : BaseViewModel() {
    var mrkresult = MutableLiveData<BaseResModel<SubmitResult>>()
    var agvtype = MutableLiveData<BaseResModel<AgvLocationType>>()
    var wjresult = MutableLiveData<BaseResModel<SubmitResult>>()
    var agvresult = MutableLiveData<BaseResModel<SubmitResult>>()

    //五金仓库，直接提交
    fun getOrderNo(dj: String, model: InstorageModel) {
        viewModelScope.launch {
            try {
                showLoading()
                val resultmodel = withContext(Dispatchers.IO) { httpUtil.getOrderNo(dj) }
                if (resultmodel.code == 1000) {
                    model.swh = resultmodel.data!!.list!![0].DJH!!
                    val rkresult = withContext(Dispatchers.IO) { httpUtil.inStorage(model) }
                    wjresult.value = rkresult
                } else {
                    showError(ErrorResult(resultmodel.code, resultmodel.msg, true))
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

    //Agv仓库提交
    fun getOrderNoAgvSubmit(dj: String, model: InstorageModel) {

        viewModelScope.launch {
            showLoading()
            val resultmodel = withContext(Dispatchers.IO) { httpUtil.getOrderNo(dj) }
            if (resultmodel.code == 1000) {
                model.swh = resultmodel.data!!.list!![0].DJH!!
                val rkresult = withContext(Dispatchers.IO) { httpUtil.inStorage(model) }
                mrkresult.value = rkresult
                dismissLoading()
            } else {
                showError(ErrorResult(resultmodel.code, resultmodel.msg, true))
            }


        }
    }

    //agv任务类型
    fun subAgvType(YWLXM: String) {
        viewModelScope.launch {
            val agvModel = withContext(Dispatchers.IO) { httpUtil.getPointgetType(YWLXM) }
            agvtype.value = agvModel
        }

    }

    //agv任务提交
    fun agvTaskadd(agvmodel: AgvSubmitModel) {
        viewModelScope.launch {
            agvresult.value = withContext(Dispatchers.IO) { httpUtil.addAGV(agvmodel) }
        }
    }


}