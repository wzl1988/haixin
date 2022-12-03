package com.eohi.hx.ui.work.quality.finish

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
import okhttp3.MultipartBody

class FinishViewModel : BaseViewModel() {

    var finishCheckResult = MutableLiveData<ArrayList<FinishCheckListResult>>()
    var detailmodel = MutableLiveData<CommonDetailModel>()
    var gdhList = MutableLiveData<ArrayList<GDListBean>>()
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()
    var response = MutableLiveData<ArrayList<SubmitResult>>()
    var deleteFinishCheck = MutableLiveData<DeleteResult>()
    var blxxList = MutableLiveData<ArrayList<BlxxBean>>()
    var InspectionitemModelList = MutableLiveData<ArrayList<InspectionitemModel>>()
    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxList)
    }

    fun delete(gdh: String, jydh: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { httpUtil.deleteFinishCheck(gdh, jydh) }
            if (result.code == 200) {//请求成功
                deleteFinishCheck.value = result.data?.list!![0]
            } else {
                LogUtil.e("请求错误>>" + result.msg)
                showError(ErrorResult(result.code, result.msg, true))
            }
        }
    }

    fun modifyFinishCheck(commonPostModel: CommonPostModel) {
        launchList(
            { httpUtil.modifyFinishCheck(commonPostModel) },
            response,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getDetailGDH(gsh: String, gdh: String) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getFinishMessageGDH(gsh, gdh)
            }
            if (result.code == 1000) {
                detailmodel.value = result
            } else {
                showError(ErrorResult(result.code, result.msg, true))
            }
            dismissLoading()
        }
    }

    fun postFinishCheck(commonPostModel: CommonPostModel) {
        launchList(
            { httpUtil.postFinish(commonPostModel) },
            response,
            isShowLoading = true,
            isShowError = true,
        )
    }

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

    fun getResult(file: MultipartBody) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { httpUtil.fileUpload(file) }
                if (result.code == 200) {//请求成功
                    resultFile.value = result.data
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

    fun getFinishCheckGDHList(gsh: String) {
        launchList(
            { httpUtil.getFinishCheckGDH(gsh) },
            gdhList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getFinishCheckDetail(gsh: String, gdh: String, djh: String) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getFinishCheckDetail(gsh, gdh, djh)
            }
            if (result.code == 1000) {
                detailmodel.value = result
            } else {
                showError(ErrorResult(result.code, result.msg, true))
            }
            dismissLoading()
        }
    }

    fun getFinishCheckList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getFinishCheckList(hashMap) },
            finishCheckResult,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getInspectionItems(wph:String,gsh:String){
        launchList(
            { httpUtil.getInspectionItems(wph,gsh) },
            InspectionitemModelList,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }



}