package com.eohi.hx.ui.main.agv.pick

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.PickingWpxxModel
import com.eohi.hx.ui.work.model.PointgetCkModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/11 10:54
 */
class AgvSendingViewModel : BaseViewModel() {
    var cklist = MutableLiveData<ArrayList<PointgetCkModel>>()
    var itemlist = MutableLiveData<ArrayList<PickingWpxxModel>>()

    //点位的仓库号和库位号
    fun getCklist(dw: String) {
        launchList({ httpUtil.pointToGetCk(dw) }, cklist, true)
    }

    //获取物品
    fun getItemInfo(tmh: String, ckh: String, kwh: String) {
        launchList({ httpUtil.getMoveItemInfo(tmh, ckh, kwh) }, itemlist, true)
    }

}