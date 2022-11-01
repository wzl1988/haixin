package com.eohi.hx.ui.work.quality.unqualified

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*

class UnQualifiedReportViewModel : BaseViewModel() {

    val lzkDetailResult = MutableLiveData<ArrayList<LzkDetailResult>>()
    val blxxResult = MutableLiveData<ArrayList<BlxxBean>>()
    val gxResult = MutableLiveData<ArrayList<GxModel>>()
    val personResult = MutableLiveData<ArrayList<PersonModel>>()
    val postResult = MutableLiveData<ArrayList<SubmitResult>>()

    fun getLzkDetail(cardno: String) {
        launchList({ httpUtil.getLzkDetail(cardno) }, lzkDetailResult, true, successCode = 200)
    }

    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxResult, true)
    }

    fun getGx(cardno: String) {
        launchList({ httpUtil.getGx(cardno) }, gxResult, true, successCode = 200)
    }

    //选择人员
    fun getPerson(gsh: String, username: String) {
        launchList(
            { httpUtil.getPersonnelInfo(gsh, username) },
            personResult,
            true,
            successCode = 200
        )
    }

    fun post(unQualifiedPostModel: UnQualifiedPostModel) {
        launchList({ httpUtil.unQualifiedPost(unQualifiedPostModel) }, postResult, true)
    }

}