package com.eohi.hx.ui.work.quality.repair

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/12/12 10:16
 */
class RepairDisposeViewModel :BaseViewModel() {
    var detailmodel = MutableLiveData<CommonDetailModel>()
    var personlist = MutableLiveData<ArrayList<PersonModel>>()
    var response = MutableLiveData<ArrayList<SubmitResult>>()
    fun getDetailTMH(gsh: String, tmh: String) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getFinishMessageTMH(gsh, tmh)
            }
            if (result.code == 1000) {
                detailmodel.value = result
            } else {
                showError(ErrorResult(result.code, result.msg, true))
            }
            dismissLoading()
        }
    }


    //选择人员
    fun getPerson(gsh: String, username: String) {
        launchList(
            { httpUtil.getPersonnelInfo(gsh, username) },
            personlist,
            isShowLoading = true,
            successCode = 200
        )
    }


    fun postRepairModel(commonPostModel: RepairPostModel) {
        launchList(
            { httpUtil.postRepairModel(commonPostModel) },
            response,
            isShowLoading = true,
            isShowError = true
        )
    }


}