package com.eohi.hx.ui.work.purchasein.retreat

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.main.model.KwModel
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.ui.work.model.PickingWpxxModel
import com.eohi.hx.ui.work.model.ReasonModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/24 21:05
 */
class PurchaseByCodeViewModel : BaseViewModel() {
    var whlist = MutableLiveData<ArrayList<WarehouseInfo>>()

    var kwlist = MutableLiveData<ArrayList<KwModel>>()

    var itemlist = MutableLiveData<ArrayList<PickingWpxxModel>>()

    var reasonlist = MutableLiveData<ArrayList<ReasonModel>>()

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
        launchList({ httpUtil.getTkyy("UC") }, reasonlist, true)
    }


}