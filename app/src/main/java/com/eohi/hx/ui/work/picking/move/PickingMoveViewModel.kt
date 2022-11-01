package com.eohi.hx.ui.work.picking.move

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.PickingWpxxModel
import com.eohi.hx.ui.work.model.PointgetCkModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/13 15:59
 */
class PickingMoveViewModel : BaseViewModel() {
    var cklist = MutableLiveData<ArrayList<PointgetCkModel>>()
    var endcklist = MutableLiveData<ArrayList<PointgetCkModel>>()
    var itemlist = MutableLiveData<ArrayList<PickingWpxxModel>>()
    fun getCklist(dw: String) {
        launchList({ httpUtil.pointToGetCk(dw) }, cklist, true)
    }

    fun getEndList(dw: String) {
        launchList({ httpUtil.pointToGetCk(dw) }, endcklist, true)
    }

    fun getItemInfo(tmh: String, ckh: String, kwh: String) {
        launchList({ httpUtil.getMoveItemInfo(tmh, ckh, kwh) }, itemlist, true)
    }

}