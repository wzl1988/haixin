package com.eohi.hx.ui.work.sales.retreat

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.main.model.KwModel
import com.eohi.hx.ui.main.model.TkItemModel
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.ui.work.model.KhModel
import com.eohi.hx.ui.work.model.PickingWpxxModel
import com.eohi.hx.ui.work.model.ReasonModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/24 10:57
 */
class SalesRetreatViewModel : BaseViewModel() {
    var whlist = MutableLiveData<ArrayList<WarehouseInfo>>()

    var kwlist = MutableLiveData<ArrayList<KwModel>>()

    var itemlist = MutableLiveData<ArrayList<PickingWpxxModel>>()

    var reasonlist = MutableLiveData<ArrayList<ReasonModel>>()

    var khhlist = MutableLiveData<ArrayList<KhModel>>()
    var mItemLsit = MutableLiveData<ArrayList<TkItemModel>>()

    //获取库仓库列表
    fun getCklist(isShowLoading: Boolean = true) {
        launchList({ httpUtil.getWarehouseInfo() }, whlist, isShowLoading)
    }

    //根据仓库获取库位
    fun getKwlist(ckh: String?, isShowLoading: Boolean = true) {
        launchList({ httpUtil.getKwList(ckh) }, kwlist, isShowLoading)
    }

    fun getItemInfo(tmh: String, ckh: String, kwh: String) {
        launchList({ httpUtil.getMoveItemInfo(tmh, ckh, kwh) }, itemlist, true)
    }

    //退库原因
    fun getTkyym() {
        launchList({ httpUtil.getTkyy("XR") }, reasonlist, true)
    }

    fun getKhh(gsh: String, content: String) {
        launchList({ httpUtil.getKhh(gsh, content) }, khhlist, true)
    }


    fun getNewItemInfo(gsh: String, tmh: String) {
        launchList({ httpUtil.getTkItemInfo(gsh, tmh) }, mItemLsit, true)
    }


}