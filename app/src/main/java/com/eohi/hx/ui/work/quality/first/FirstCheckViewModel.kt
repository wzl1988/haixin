package com.eohi.hx.ui.work.quality.first

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.work.model.*
import com.eohi.hx.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class FirstCheckViewModel : BaseViewModel() {

    var firstCheckList = MutableLiveData<ArrayList<FirstCheckListResult>>()
    var odlchecklist =  MutableLiveData<ArrayList<OldFirstCheckListResult>>()
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()
    var dataList = MutableLiveData<CommonDetailModel>()
    var firstCheckPostResult = MutableLiveData<BaseResModel<SubmitResult>>()
    var deleteFirstCheck = MutableLiveData<DeleteResult>()
    var blxxList = MutableLiveData<ArrayList<BlxxBean>>()
    var jylxBean = MutableLiveData<ArrayList<JylxBean>>()
    var InspectionitemModelList = MutableLiveData<ArrayList<InspectionitemModel>>()
    fun getJylx(flbm: String) {
        launchList({ httpUtil.getJylx(flbm) }, jylxBean)
    }

    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxList)
    }

    fun delete(rwdh: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { httpUtil.deleteFirstCheck(rwdh) }
            if (result.code == 200) {//请求成功
                deleteFirstCheck.value = result.data?.list!![0]
            } else {
                LogUtil.e("请求错误>>" + result.msg)
                showError(ErrorResult(result.code, result.msg, true))
            }
        }
    }

    fun modify(model: CommonPostModel) {
        showLoading()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    httpUtil.modifyFirstCheck(model)
                }
                firstCheckPostResult.value = result
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

    fun getDetail(hashMap: HashMap<String, String>) {
        showLoading()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    httpUtil.firstCheckDetail(hashMap)
                }
                dataList.value = result
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

    fun postCheckFirst(commonPostModel: CheckPostModel) {
        showLoading()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    httpUtil.postFirstCheck(commonPostModel)
                }
                if(result.code==200){
                    firstCheckPostResult.value = result
                }else{
                    showError(ErrorResult(result.code, result.msg, true))
                }
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

    fun getDataList(hashMap: HashMap<String, String>) {
        showLoading()
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    httpUtil.getFirstCheckData(hashMap)
                }
                dataList.value = result
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

    //未启用
    fun getFirstCheckList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getFirstCheckList(hashMap) },
            firstCheckList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getFirstCheckList(userid:String) {
        launchList(
            { httpUtil.getFirstCheckList(userid) },
            firstCheckList,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun getFirstCheckResultList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getFirstCheckResultList(hashMap) },
            odlchecklist,
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