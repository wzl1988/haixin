package com.eohi.hx.ui.work.inventory

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.main.model.KwModel
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.ui.work.model.PickingWpxxModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/21 11:12
 */
class InventoryAdjustmentViewModel : BaseViewModel() {

    var whlist = MutableLiveData<ArrayList<WarehouseInfo>>()

    var kwlist = MutableLiveData<ArrayList<KwModel>>()

    var itemlist = MutableLiveData<ArrayList<PickingWpxxModel>>()

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


}