package com.eohi.hx.ui.main.agv

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.InstorageModel
import com.eohi.hx.ui.work.model.SubmitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/3 10:28
 */
class AgvInDetailViewModel : BaseViewModel() {
    var mrkresult = MutableLiveData<BaseResModel<SubmitResult>>()
    var agvresult = MutableLiveData<BaseResModel<SubmitResult>>()

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


    //agv任务提交
    fun agvTaskadd(agvmodel: AgvSubmitModel) {
        viewModelScope.launch {
            agvresult.value = withContext(Dispatchers.IO) { httpUtil.addAGV(agvmodel) }
        }
    }


}