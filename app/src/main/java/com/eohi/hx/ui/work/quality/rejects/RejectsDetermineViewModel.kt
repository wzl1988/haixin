package com.eohi.hx.ui.work.quality.rejects

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.BlxxBean
import com.eohi.hx.ui.work.model.BlyyBean
import com.eohi.hx.ui.work.model.EquipmentsModel
import com.eohi.hx.ui.work.quality.rejects.model.GoodListItemModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/11/10 9:50
 */
class RejectsDetermineViewModel:BaseViewModel() {
    var blxxList = MutableLiveData<ArrayList<BlxxBean>>()
    var sblist = MutableLiveData<ArrayList<EquipmentsModel>>()
    var itemlist = MutableLiveData<ArrayList<GoodListItemModel>>()
    var blyyList = MutableLiveData<ArrayList<BlyyBean>>()
    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxList)
    }

    fun getBlyy(){
        launchList({ httpUtil.getBlyy() }, blyyList)
    }


    fun getSblist(companycode: String, userid: String, scxbh: String) {
        launchList(
            { httpUtil.getNewSBxx(companycode, userid, scxbh) },
            sblist,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getDisposalItemList(rwdh:String){
        launchList(
            { httpUtil.getDisposalItemList(rwdh) },
            itemlist,
            isShowLoading = true,
            successCode = 200
        )
    }




}