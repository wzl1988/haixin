package com.eohi.hx.ui.work.process.coordination

import com.eohi.hx.base.BaseFragment
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.FragmentCoordinationStartBinding
import com.eohi.hx.ui.work.model.WxkgInfoModel
import com.eohi.hx.utils.DateUtil

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/21 9:38
 */
class CoordinationFinishedFragment: BaseFragment<BaseViewModel, FragmentCoordinationStartBinding>() {
    var cardNo:String = ""
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
    }

    override fun initView() {
    }

    override fun initClick() {
    }

    override fun initData() {
        v.tvUsername.text = username
        v.tvDate.text = DateUtil.data
    }

    fun setData(data: WxkgInfoModel){
        v.tvWph.text = data.wph
        v.tvWpmc.text = data.wpmc
        v.tvGg.text = data.gg
        v.tvSl.text = data.sybzs.toString()
        v.etGx.text = data.gxms
        cardNo = data.lzkkh
    }


    override fun lazyLoadData() {
    }
}