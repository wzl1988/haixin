package com.eohi.hx.ui.work.tooling.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.SubmitResult
import com.eohi.hx.ui.work.tooling.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/10/9 9:42
 */
class ToolOnlineViewModel:BaseViewModel() {
    var toollist = MutableLiveData<ArrayList<ToolListModel>>()
    var offlineResult = MutableLiveData<ArrayList<SubmitResult>>()
    var onLineResult = MutableLiveData<ArrayList<SubmitResult>>()
    var gzxx = MutableLiveData<ArrayList<ToolinfoModel>>()
    var sbbh:String =""
    fun getToolList(barcode: String){
        viewModelScope.launch {
            try {
                showLoading()
                val equipment= with(Dispatchers.IO){
                    httpUtil.getToolEquipment(barcode)
                }
                if(equipment.code ==200){
                    val equipModel= equipment?.data!!.list?.get(0)
                    val sbbm=equipModel?.SBBM
                    sbbh= equipModel?.SBBM.toString()
                    if (sbbm != null) {
                        val toolresult = with(Dispatchers.IO) {
                            httpUtil.getToolOnlinelist("019-017")
                        }
                        if(toolresult.code == 1000){
                            toollist.value = toolresult.data!!.list
                        }
                    }
                }else{
                    showError(ErrorResult(equipment.code, equipment.msg, true))
                }

            }catch(e:Exception){
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            }finally {
                dismissLoading()
            }

        }
    }

    //根据设备号，获取下面工装列表
    fun getToolListBySbbh(sbbh:String){
        launchList({httpUtil.getToolOnlinelist(sbbh)},toollist)
    }


    //工装下线
    fun offlineSubmit(model:ToolOfflineSubmitmodel){
        launchList({httpUtil.subMitToolOffline(model)},offlineResult)
    }

    //获取工装信息
    fun getGzxx(gzbm:String,gsh:String){
        launchList({httpUtil.getGzxx(gzbm,gsh)},gzxx,isShowLoading = true,successCode = 200)
    }

    //工装上线
    fun onLineSubmit(model: ToolOnlineSubmitModel){
        launchList({httpUtil.submitToolOnline(model)},offlineResult)
    }

}