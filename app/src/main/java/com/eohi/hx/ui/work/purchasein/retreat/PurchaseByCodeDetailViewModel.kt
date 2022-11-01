package com.eohi.hx.ui.work.purchasein.retreat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.hx.api.error.ErrorResult
import com.eohi.hx.api.error.ErrorUtil
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.work.model.SubmitResult
import com.eohi.hx.ui.work.model.TkSubmitModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/24 21:05
 */
class PurchaseByCodeDetailViewModel : BaseViewModel() {
    var wjresult = MutableLiveData<BaseResModel<SubmitResult>>()

    //提交
    fun submit(dj: String, model: TkSubmitModel) {
        viewModelScope.launch {
            try {
                showLoading()
                val resultmodel = withContext(Dispatchers.IO) { httpUtil.getOrderNo(dj) }
                if (resultmodel.code == 1000) {
                    model.swh = resultmodel.data!!.list!![0].DJH!!
                    val rkresult = withContext(Dispatchers.IO) { httpUtil.tkSubmit(model) }
                    wjresult.value = rkresult
                } else {
                    showError(ErrorResult(resultmodel.code, resultmodel.msg, true))
                }
            } catch (e: Exception) {
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }

        }
    }

}