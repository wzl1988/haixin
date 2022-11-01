package com.eohi.hx.ui.work.tooling.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.ui.work.model.SubmitResult
import com.eohi.hx.ui.work.tooling.model.ToolBackSubmit
import com.eohi.hx.ui.work.tooling.model.ToolCkModel
import com.eohi.hx.ui.work.tooling.model.ToolinfoModel
import com.eohi.hx.ui.work.tooling.model.ToolingBackReason

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/13 15:32
 */
class ToolingBackViewModel : BaseViewModel() {
    var reasonlist = MutableLiveData<ArrayList<ToolingBackReason>>()
    var toolinfo = MutableLiveData<ArrayList<ToolinfoModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()
    var ckh = MutableLiveData<ArrayList<ToolCkModel>>()

    //归还原因
    fun getGhyy() {
        launchList(
            { httpUtil.getBackReanson() },
            reasonlist,
            isShowLoading = true,
            successCode = 200
        )
    }

    //归还信息
    fun getToolInfo(gzbm: String, gsh: String) {
        launchList(
            { httpUtil.getToolbackInfo(gzbm, gsh) },
            toolinfo,
            isShowLoading = true,
            successCode = 200
        )
    }

    //归还提交
    fun submit(model: ToolBackSubmit) {
        launchList(
            { httpUtil.submitToolback(model) },
            result,
            isShowLoading = true,
            successCode = 200
        )
    }

    //仓库号
    fun getCkh(kwh: String) {
        launchList({ httpUtil.getCkh(kwh) }, ckh, isShowLoading = true)
    }


}