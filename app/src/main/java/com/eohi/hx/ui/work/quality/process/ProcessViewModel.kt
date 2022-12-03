package com.eohi.hx.ui.work.quality.process

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

class ProcessViewModel : BaseViewModel() {

    var processList = MutableLiveData<ArrayList<ProcessCheckListResult>>()
    var detailmodel = MutableLiveData<CommonDetailModel>()
    var gxList = MutableLiveData<ArrayList<GXListBean>>()
    var cxList = MutableLiveData<ArrayList<CxBean>>()
    var gdList = MutableLiveData<ArrayList<GDListBean>>()
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()
    var gdResult = MutableLiveData<CommonDetailModel>()
    var response = MutableLiveData<ArrayList<SubmitResult>>()
    var deleteProcess = MutableLiveData<DeleteResult>()
    var blxxList = MutableLiveData<ArrayList<BlxxBean>>()
    var jylxBean = MutableLiveData<ArrayList<JylxBean>>()
    var kgInfoList = MutableLiveData<ArrayList<KgInfoModel>>()
    var InspectionitemModelList = MutableLiveData<ArrayList<InspectionitemModel>>()
    fun getJylx(flbm: String) {
        launchList({ httpUtil.getJylx(flbm) }, jylxBean)
    }

    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxList)
    }

    fun getKgInfo(cardno: String) {
        launchList(
            { httpUtil.getKgInfoByCard(cardno) },
            kgInfoList,
            isShowLoading = true,
            successCode = 200
        )
    }


    fun delete(gdh: String, jydh: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { httpUtil.deleteProcess(gdh, jydh) }
            if (result.code == 200) {//请求成功
                deleteProcess.value = result.data?.list!![0]
            } else {
                LogUtil.e("请求错误>>" + result.msg)
                showError(ErrorResult(result.code, result.msg, true))
            }
        }
    }

    fun modifyResponse(model: CommonPostModel) {
        launchList(
            {
                httpUtil.modifyProcess(model)
            },
            response,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun postResponse(model: CheckPostModel) {
        launchList(
            {
                httpUtil.postGCXJ(model)
            },
            response,
            isShowLoading = true,
            isShowError = true,
        )
    }

    fun getGDResultList(gsh: String, GDH: String) {
        showLoading()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    httpUtil.getGDResult(gsh, GDH)
                }
                gdResult.value = result
                dismissLoading()
            } catch (e: Exception) {
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {
                dismissLoading()
            }
        }
    }

    fun getGxList(gsh: String, GDH: String) {
        launchList(
            { httpUtil.getGXList(gsh, GDH) },
            gxList,
            isShowLoading = true,
            isShowError = true
        )
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

    fun getCXList(gsh: String) {
        launchList(
            { httpUtil.getGCCXList(gsh) },
            cxList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getGDList(gsh: String) {
        launchList(
            { httpUtil.getGDList(gsh) },
            gdList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getProcessList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getProcessList(hashMap) },
            processList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getDetail(gsh: String, gdh: String, gxh: String, djh: String) {
        showLoading()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    httpUtil.getGCXJDetail(gsh, gdh, gxh, djh)
                }
                detailmodel.value = result
                dismissLoading()
            } catch (e: Exception) {
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {
                dismissLoading()
            }
        }
    }



    fun getInspectionItems(wph:String,gxh:String,gsh:String){
        launchList(
            { httpUtil.getProcessInsList(wph,gxh,gsh) },
            InspectionitemModelList,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }


}