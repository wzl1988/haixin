package com.eohi.hx.ui.work.quality.rejects

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*
import com.eohi.hx.ui.work.quality.rejects.model.GoodListItemModel
import com.eohi.hx.ui.work.quality.rejects.model.RejectsDetermineSubmitModel
import com.eohi.hx.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/10 9:50
 */
class RejectsDetermineViewModel:BaseViewModel() {
    var blxxList = MutableLiveData<ArrayList<BlxxBean>>()
    var sblist = MutableLiveData<ArrayList<EquipmentsModel>>()
    var itemlist = MutableLiveData<ArrayList<GoodListItemModel>>()
    var blyyList = MutableLiveData<ArrayList<BlyyBean>>()
    val gxResult = MutableLiveData<ArrayList<ProductionProcessesModel>>()
    val personResult = MutableLiveData<ArrayList<PersonModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()
    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxList)
    }

    fun getBlyy(){
        launchList({ httpUtil.getBlyy() }, blyyList)
    }


    fun getSblist(companycode: String, userid: String, scxbh: String) {
        launchList(
            { httpUtil.getNewSBxx(companycode, userid, scxbh) },
            sblist,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getDisposalItemList(rwdh:String){
        launchList(
            { httpUtil.getDisposalItemList(rwdh) },
            itemlist,
            isShowLoading = true,
            successCode = 200
        )
    }


    fun getGx(cardno: String) {
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

    fun post(model: RejectsDetermineSubmitModel){
        launchList({httpUtil.postRejects(model)},result, true)
    }



}