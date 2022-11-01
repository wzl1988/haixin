package com.eohi.hx.ui.work.process.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/6 9:57
 */
class ProductionRegistrationViewModel : BaseViewModel() {
    var cardinfo = MutableLiveData<ArrayList<TransferCardInfoModel>>()
    var process = MutableLiveData<ArrayList<ProductionProcessesModel>>()
    var sblist = MutableLiveData<ArrayList<EquipmentModel>>()
    var equipmentList = MutableLiveData<ArrayList<EquipmentsModel>>()
    var jgdylist = MutableLiveData<ArrayList<ProcessingUnitModel>>()

    //关联工序
    var glgxlist = MutableLiveData<ArrayList<AssociatedprocessModel>>()
    var personlist = MutableLiveData<ArrayList<PersonModel>>()
    var material = MutableLiveData<ArrayList<MaterialModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()
    var teams = MutableLiveData<ArrayList<ProductionTeamModel>>()
    var zzrlist = MutableLiveData<ArrayList<PersonModel>>()
    fun getCardInfo(cardno: String, userid: String) {
        launchList(
            { httpUtil.getLzkxx(cardno, userid) },
            cardinfo,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getProductionProcesses(cardno: String, userid: String) {
        launchList(
            { httpUtil.getScgx(cardno, userid) },
            process,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getSblist(companycode: String, userid: String) {
        launchList(
            { httpUtil.getSbxx(companycode, userid) },
            sblist,
            isShowLoading = true,
            successCode = 200
        )
    }


    fun getJgdy(cardno: String, gxno: String, equno: String) {
        if (equno.isEmpty()) {
            launchList(
                { httpUtil.getJgdysb(cardno, gxno) },
                jgdylist,
                isShowLoading = true,
                successCode = 200
            )
        } else {
            launchList(
                { httpUtil.getJgdy(cardno, gxno, equno) },
                jgdylist,
                isShowLoading = true,
                successCode = 200
            )
        }

    }

    fun getAllEquipmentList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getAllEquipments(hashMap) },
            equipmentList,
            isShowLoading = true,
            successCode = 200
        )
    }

    //关联工序
    fun getGlgx(cardno: String, gxno: String) {
        launchList(
            { httpUtil.getGlgx(cardno, gxno) },
            glgxlist,
            isShowLoading = true,
            successCode = 200
        )
    }

    //选择人员
    fun getPerson(gsh: String, username: String) {
        launchList(
            { httpUtil.getPersonnelInfo(gsh, username) },
            personlist,
            isShowLoading = true,
            successCode = 200
        )
    }

    //材料消耗
    fun getMaterial(clkno: String, userid: String) {
        launchList(
            { httpUtil.getMaterialInfo(clkno, userid) },
            material,
            isShowLoading = true,
            successCode = 200
        )
    }

    //生产登记提交
    fun submit(proCardScdjAddList: ProductionSubList) {
        launchList(
            { httpUtil.submitScdj(proCardScdjAddList) },
            result,
            isShowLoading = true,
            successCode = 200
        )
    }

    //生产班组
    fun getProductTeam(username: String) {
        launchList(
            { httpUtil.getProductTeam(username, 100, 1) },
            teams, isShowLoading = true,
            successCode = 1000
        )
    }

    //责任人
    fun getZzrlist(gsh: String, username: String) {
        launchList(
            { httpUtil.getPersonnelInfo(gsh, username) },
            zzrlist,
            isShowLoading = true,
            successCode = 200
        )
    }

}