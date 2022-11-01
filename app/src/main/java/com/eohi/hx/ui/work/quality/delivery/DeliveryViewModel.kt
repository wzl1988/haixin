package com.eohi.hx.ui.work.quality.delivery

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

class DeliveryViewModel : BaseViewModel() {

    var deliveryCheckResult = MutableLiveData<ArrayList<DeliveryCheckListResult>>()
    var detail = MutableLiveData<CommonDetailModel>()
    var gdhList = MutableLiveData<ArrayList<GDListBean>>()
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()
    var detailmodel = MutableLiveData<CommonDetailModel>()
    var response = MutableLiveData<ArrayList<SubmitResult>>()
    var deleteDeliveryCheck = MutableLiveData<DeleteResult>()
    var blxxList = MutableLiveData<ArrayList<BlxxBean>>()

    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxList)
    }

    fun delete(gdh: String, jydh: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { httpUtil.deleteDelivery(gdh, jydh) }
            if (result.code == 200) {//请求成功
                deleteDeliveryCheck.value = result.data?.list!![0]
            } else {
                LogUtil.e("请求错误>>" + result.msg)
                showError(ErrorResult(result.code, result.msg, true))
            }
        }
    }

    fun modifyDeliveryCheck(commonPostModel: CommonPostModel) {
        launchList(
            { httpUtil.modifyDelivery(commonPostModel) },
            response,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun postDeliveryCheck(commonPostModel: CommonPostModel) {
        launchList(
            { httpUtil.postDelivery(commonPostModel) },
            response,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getDeliveryDetailGDH(gsh: String, gdh: String) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getDeliveryMessageGDH(gsh, gdh)
            }
            if (result.code == 1000) {
                detailmodel.value = result
            } else {
                showError(ErrorResult(result.code, result.msg, true))
            }
            dismissLoading()
        }
    }

    fun getDeliveryDetailTMH(gsh: String, tmh: String) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getDeliveryMessageTMH(gsh, tmh)
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

    fun getDeliveryCheckResult(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getDeliveryList(hashMap) },
            deliveryCheckResult,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getDetail(gsh: String, gdh: String, djh: String) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getDeliveryDetail(gsh, gdh, djh)
            }
            if (result.code == 1000) {
                detail.value = result
            } else {
                showError(ErrorResult(result.code, result.msg, true))
            }
            dismissLoading()
        }
    }

}