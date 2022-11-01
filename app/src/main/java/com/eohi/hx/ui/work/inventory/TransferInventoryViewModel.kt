package com.eohi.hx.ui.work.inventory

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.work.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *@author: YH
 *@date: 2021/12/7
 *@desc: 调库位栈板ViewModel
 */
class TransferInventoryViewModel : BaseViewModel() {

    val transferWp = MutableLiveData<ArrayList<TransferWpModel>>()
    val transferKw = MutableLiveData<ArrayList<TransferKwModel>>()
    val transferKwWp = MutableLiveData<ArrayList<TransferKwWpModel>>()
    val postResult = MutableLiveData<BaseResModel<SubmitResult>>()

    fun postAgv(agvModel: AgvSubmitModel) {
        viewModelScope.launch {
            postResult.value = withContext(Dispatchers.IO) { httpUtil.addAGV(agvModel) }
        }
    }

    fun getTransferWp(tmh: String) {
        launchList(
            { httpUtil.getTransferWp(tmh) },
            transferWp,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getTransferKw(wph: String) {
        launchList(
            { httpUtil.getTransferKw(wph) },
            transferKw,
            isShowLoading = false,
            isShowError = true
        )
    }

    fun getTransferKwWp(kwh: String) {
        launchList(
            { httpUtil.getTransferKwWp(kwh) },
            transferKwWp,
            isShowLoading = true,
            isShowError = true
        )
    }

}