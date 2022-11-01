package com.eohi.hx.ui.work.process.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*

class CirculationCardViewModel : BaseViewModel() {

    var lzkxxResult = MutableLiveData<ArrayList<LzkxxModel>>()
    var postResult = MutableLiveData<ArrayList<SubmitResult>>()
    var clxhResult = MutableLiveData<ArrayList<CLXHModel>>()
    var subListResult = MutableLiveData<ArrayList<LZKSubListModel>>()
    var gxResult = MutableLiveData<ArrayList<GxResultModel>>()

    fun getGx(cardno: String) {
        launchList(
            { httpUtil.getGXResult(cardno) },
            gxResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun getSubList(cardno: String) {
        launchList(
            { httpUtil.getSubList(cardno) },
            subListResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun getCLXH(cardno: String) {
        launchList(
            { httpUtil.getCLXH(cardno) },
            clxhResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun post(lzkPostModel: LzkPostModel) {
        launchList(
            { httpUtil.postLzk(lzkPostModel) },
            postResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun getLzkxx(cardno: String) {
        launchList(
            { httpUtil.getLzkxx(cardno) },
            lzkxxResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

}