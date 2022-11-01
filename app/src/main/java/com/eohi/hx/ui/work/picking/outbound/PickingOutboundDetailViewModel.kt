package com.eohi.hx.ui.work.picking.outbound

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.main.agvmodel.ProductionlineModel
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.InstorageModel
import com.eohi.hx.ui.work.model.SubmitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/17 14:29
 */
class PickingOutboundDetailViewModel : BaseViewModel() {
    var mckresult = MutableLiveData<BaseResModel<SubmitResult>>()
    var wjresult = MutableLiveData<BaseResModel<SubmitResult>>()
    var linelist = MutableLiveData<ArrayList<ProductionlineModel>>()
    var agvresult = MutableLiveData<BaseResModel<SubmitResult>>()

    //领料提交
    fun submit(dj: String, model: InstorageModel) {
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
                val ckresult = withContext(Dispatchers.IO) { httpUtil.inStorage(model) }
                mckresult.value = ckresult
                dismissLoading()
            } else {
                showError(ErrorResult(resultmodel.code, resultmodel.msg, true))
            }

        }
    }


    //获取产线列表
    fun getLinelist(gsh: String) {
        launchList({ httpUtil.getProlineList(gsh) }, linelist, true)
    }

    //agv任务提交
    fun agvTaskadd(agvmodel: AgvSubmitModel) {
        viewModelScope.launch {
            showLoading()
            agvresult.value = withContext(Dispatchers.IO) { httpUtil.addAGV(agvmodel) }
        }
    }


}