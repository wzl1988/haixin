package com.eohi.hx.ui.work.tooling.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.SubmitResult
import com.eohi.hx.ui.work.tooling.model.ToolPersonInfoModel
import com.eohi.hx.ui.work.tooling.model.ToolRecipientsReasonModel
import com.eohi.hx.ui.work.tooling.model.ToolRecipientsSubmit
import com.eohi.hx.ui.work.tooling.model.ToolinfoModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/12 13:19
 */
class ToolingRecipientsViewModel : BaseViewModel() {
    var lyyylist = MutableLiveData<ArrayList<ToolRecipientsReasonModel>>()
    var gzxx = MutableLiveData<ArrayList<ToolinfoModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()
    var personlist = MutableLiveData<ArrayList<ToolPersonInfoModel>>()

    //获取工装领用原因
    fun getLyyy() {
        launchList({ httpUtil.getGzlyyy() }, lyyylist, isShowLoading = true, successCode = 200)
    }

    //获取工装信息
    fun getGzxx(gzbm: String, gsh: String) {
        launchList({ httpUtil.getGzxx(gzbm, gsh) }, gzxx, isShowLoading = true, successCode = 200)
    }

    //申请人列表
    fun getPersonList(gsh: String, ygxm: String) {
        launchList(
            { httpUtil.getSqrList(gsh, ygxm) },
            personlist,
            isShowLoading = true,
            successCode = 200
        )
    }

    //提交
    fun submit(model: ToolRecipientsSubmit) {
        launchList(
            { httpUtil.submitToolRecipients(model) },
            result,
            isShowLoading = true,
            successCode = 200
        )
    }
}