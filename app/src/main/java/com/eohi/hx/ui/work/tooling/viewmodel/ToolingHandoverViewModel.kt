package com.eohi.hx.ui.work.tooling.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.SubmitResult
import com.eohi.hx.ui.work.tooling.model.ToolPersonInfoModel
import com.eohi.hx.ui.work.tooling.model.ToolhandoverModel
import com.eohi.hx.ui.work.tooling.model.ToolhandoverSubModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/14 14:08
 */
class ToolingHandoverViewModel : BaseViewModel() {

    var handoverinfo = MutableLiveData<ArrayList<ToolhandoverModel>>()
    var personlist = MutableLiveData<ArrayList<ToolPersonInfoModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()

    fun getToolhandoverInfo(gzbm: String, gsh: String) {
        launchList(
            { httpUtil.getToolHandover(gzbm, gsh) },
            handoverinfo,
            isShowLoading = true,
            successCode = 200
        )
    }

    //接收人列表
    fun getPersonList(gsh: String, ygxm: String) {
        launchList(
            { httpUtil.getSqrList(gsh, ygxm) },
            personlist,
            isShowLoading = true,
            successCode = 200
        )
    }

    //交接提交
    fun submitHandover(model: ToolhandoverSubModel) {
        launchList(
            { httpUtil.submitHandover(model) },
            result,
            isShowLoading = true,
            successCode = 200
        )
    }

}