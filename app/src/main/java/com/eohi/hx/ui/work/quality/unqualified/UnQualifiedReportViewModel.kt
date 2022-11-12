package com.eohi.hx.ui.work.quality.unqualified

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*
import com.eohi.hx.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnQualifiedReportViewModel : BaseViewModel() {

    val lzkDetailResult = MutableLiveData<ArrayList<LzkDetailResult>>()
    val blxxResult = MutableLiveData<ArrayList<BlxxBean>>()
    val gxResult = MutableLiveData<ArrayList<ProductionProcessesModel>>()
    val personResult = MutableLiveData<ArrayList<PersonModel>>()
    val postResult = MutableLiveData<ArrayList<SubmitResult>>()
    var sblist = MutableLiveData<ArrayList<EquipmentsModel>>()
    fun getLzkDetail(cardno: String) {
        launchList({ httpUtil.getLzkDetail(cardno) }, lzkDetailResult, true, successCode = 200)
    }

    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxResult, true)
    }

    fun getGx(cardno: String) {
//        launchList({ httpUtil.getScgx(cardno,"") }, gxResult, true, successCode = 200)

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { httpUtil.getGx(cardno, "")}
                if (result.code == 200) {//请求成功
                    gxResult.value = result.data.zrgx
                } else {
                    LogUtil.e("请求错误>>" + result.msg)
                    showError(ErrorResult(result.code, result.msg, true))
                }
            } catch (e: Throwable) {//接口请求失败
                LogUtil.e("请求异常>>" + e.message)
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }


        }
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

    fun getSblist(companycode: String, userid: String, scxbh: String) {
        launchList(
            { httpUtil.getNewSBxx(companycode, userid, scxbh) },
            sblist,
            isShowLoading = true,
            successCode = 200
        )
    }

}