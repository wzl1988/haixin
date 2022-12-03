package com.eohi.hx.ui.work.process.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*

class StartWorkViewModel : BaseViewModel() {

    var kgInfoList = MutableLiveData<ArrayList<KgInfoModel>>()
    var equipmentList = MutableLiveData<ArrayList<EquipmentsModel>>()
    var jgdyList = MutableLiveData<ArrayList<JgdyModel>>()
    var gxList = MutableLiveData<ArrayList<GxModel>>()
    var kgpostresult = MutableLiveData<ArrayList<SubmitResult>>()
    var suspendresult = MutableLiveData<ArrayList<SubmitResult>>()
    var recoverresult = MutableLiveData<ArrayList<SubmitResult>>()
    fun startWork(kgPostModel: KgPostModel) {
        launchList(
            { httpUtil.postKg(kgPostModel) },
            kgpostresult,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun postSuspend(kgPostModel: KgPostModel){
        launchList(
            { httpUtil.postSuspend(kgPostModel) },
            suspendresult,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun postRecover(kgPostModel: KgPostModel){
        launchList(
            { httpUtil.postRecover(kgPostModel) },
            recoverresult,
            isShowLoading = true,
            successCode = 200
        )
    }



    fun getKgInfo(cardno: String) {
        launchList(
            { httpUtil.getKgInfoByCard(cardno) },
            kgInfoList,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getPersonalEquipmentList(companycode: String, userid: String, scxbh: String) {
        launchList(
            { httpUtil.getNewSBxx(companycode, userid, scxbh) },
            equipmentList,
            isShowLoading = true,
            successCode = 200
        )
    }


    fun getAllEquipmentList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getAllEquipments(hashMap) },
            equipmentList,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getJgdyNoEquipmentList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getJgdyNoEquipment(hashMap) },
            jgdyList,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getJgdyList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getJgdy(hashMap) },
            jgdyList,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getGxList(cardno: String) {
        launchList(
            { httpUtil.getGx(cardno) },
            gxList,
            isShowLoading = true,
            successCode = 200
        )
    }


}