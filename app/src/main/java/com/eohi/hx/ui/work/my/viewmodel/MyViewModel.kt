package com.eohi.hx.ui.work.my.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.SubmitResult
import com.eohi.hx.ui.work.my.model.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/18 10:45
 */
class MyViewModel : BaseViewModel() {
    //生产小组
    var scxzlist = MutableLiveData<ArrayList<MyProductionTeamModel>>()
    var unitlist = MutableLiveData<ArrayList<MyProcessingUnitModel>>()
    var deteleJgdy = MutableLiveData<ArrayList<SubmitResult>>()
    var addjgdy = MutableLiveData<ArrayList<SubmitResult>>()
    var alljgdy = MutableLiveData<ArrayList<AllProcessingUnitModel>>()
    var deleteResult = MutableLiveData<ArrayList<SubmitResult>>()
    var cylist = MutableLiveData<ArrayList<ProductionPersonModel>>()
    var addResult = MutableLiveData<ArrayList<SubmitResult>>()
    var groupMembers = MutableLiveData<ArrayList<GroupMemberModel>>()

    fun getScxzList(ZYXM: String, pageSize: Int, pageIndex: Int) {
        launchList(
            { httpUtil.getScxzList(ZYXM, pageSize, pageIndex) },
            scxzlist,
            isShowLoading = true
        )
    }

    fun getMyjgdyList(RYXX: String, pageSize: Int, pageIndex: Int) {
        launchList(
            { httpUtil.getMyjgdylist(RYXX, pageSize, pageIndex) },
            unitlist,
            isShowLoading = false
        )
    }


    //删除加工单元
    fun deteleJgdy(GSH: String, JGDYBH: String, CJR: String) {
        launchList({ httpUtil.deteleJgdy(GSH, JGDYBH, CJR) }, deteleJgdy, isShowLoading = true)
    }

    //添加加工单元
    fun addJgdy(GSH: String, JGDYBH: String, CJR: String) {
        launchList({ httpUtil.addJgdy(GSH, JGDYBH, CJR) }, addjgdy, isShowLoading = true)
    }

    //所有加工单元
    fun getAllgjdy(RYXX: String, GSH: String) {
        launchList({ httpUtil.getAlljgdy(RYXX, GSH) }, alljgdy, isShowLoading = true)
    }


    //删除生产小组
    fun deteleTeam(XZBH: String) {
        launchList({ httpUtil.deleteXZ(XZBH) }, deleteResult, isShowLoading = true)
    }

    //添加小组成员
    fun getCylist(gsh: String) {
        launchList({ httpUtil.getSccyList(gsh) }, cylist, isShowLoading = true)
    }

    //添加生产小组
    fun addScxz(model: AddxzSubmitModel) {
        launchList({ httpUtil.addScxz(model) }, addResult, isShowLoading = true)
    }

    //修改生产小组
    fun updateScxz(model: UpdatepProcuctModel) {
        launchList({ httpUtil.updateScxz(model) }, addResult, isShowLoading = true)
    }

    //获取小组成员
    fun getGroupMember(bh: String) {
        launchList({ httpUtil.getGroupmember(bh) }, groupMembers, isShowLoading = true)
    }


}