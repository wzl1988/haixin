package com.eohi.hx.ui.work.quality.register

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/7/25 14:11
 */
class InspectionRegisterViewModel: BaseViewModel() {
    var kgInfoList = MutableLiveData<ArrayList<KgInfoModel>>()
    var person = MutableLiveData<ArrayList<PersonModel>>()
    var itemlist = MutableLiveData<ArrayList<ItemModel>>()
    var gxlist = MutableLiveData<ArrayList<GxListModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()
    fun getKgInfo(cardno: String) {
        launchList(
            { httpUtil.getKgInfoByCard(cardno) },
            kgInfoList,
            isShowLoading = true,
            successCode = 200
        )

    }

    //查询检验人员
    fun findJYRY(gsh: String, username: String) {
        launchList(
            { httpUtil.getJYRY(gsh, username) },
            person,
            isShowLoading = true,
            successCode = 200
        )
    }

    //物品列表
    fun  getItemInfo(wph:String){
        launchList(
            { httpUtil.getItemInfo(wph) },
            itemlist,
            isShowLoading = true,
            successCode = 200
        )
    }
    //工序
    fun getGXList(wph:String){
        launchList(
            { httpUtil.getGXList(wph) },
            gxlist,
            isShowLoading = true,
            successCode = 200
        )
    }
    //提交
    fun submit(model: RegisterSubmitModel){
        launchList(
            { httpUtil.postRegister(model) },
            result,
            isShowLoading = true,
            successCode = 200
        )
    }

}
