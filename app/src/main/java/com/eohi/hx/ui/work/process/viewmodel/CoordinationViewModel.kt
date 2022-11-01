package com.eohi.hx.ui.work.process.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/25 9:35
 */
class CoordinationViewModel:BaseViewModel() {
    var gyslist = MutableLiveData<ArrayList<SupplierModel>>()

    var wxkgList = MutableLiveData<ArrayList<WxkgInfoModel>>()

    var gxList = MutableLiveData<ArrayList<WxgxInfoModel>>()

    var subresult = MutableLiveData<ArrayList<SubmitResult>>()

    var wglist = MutableLiveData<ArrayList<WxkgInfoModel>>()
    //获取供应商
    fun getGysList(gsh:String,gysh:String){
        launchList({httpUtil.getWxgysList(gsh,gysh)},gyslist,isShowLoading = true,successCode = 200)
    }
    //流转卡获取信息
    fun getWxkginfo(cardno:String){
        launchList({httpUtil.getWxinfobycard(cardno)},wxkgList,isShowLoading = true,successCode = 200)
    }
    //外协工序
    fun getGxlist(cardno:String){
        launchList({httpUtil.getWxgxlist(cardno)},gxList,isShowLoading = true,successCode = 200)
    }

    //外协开工提交
    fun submit(model: WxkgSubmitModel){
        launchList({httpUtil.subMitKg(model)},subresult,isShowLoading = true,successCode = 200)
    }

    //完工
    fun submitWG(model: WxkgSubmitModel){
        launchList({httpUtil.subMitWg(model)},subresult,isShowLoading = true,successCode = 200)
    }


    //流转卡完工信息
    fun getWgxx(cardno:String){
        launchList({httpUtil.getWgInfoByCard(cardno)},wglist,isShowLoading = true,successCode = 200)
    }



}