package com.eohi.hx.ui.main.agv.abnormal

import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityRepostAgvabnormalBinding
import com.eohi.hx.event.Event
import com.eohi.hx.event.EventCode
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalListBean
import com.eohi.hx.ui.main.agvmodel.AgvAbnormalPostBean
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

/**
 *@author: YH
 *@date: 2022/1/13
 *@desc:对应agv异常状态5
 */
class RepostAgvAbnormalActivity :
    BaseActivity<AgvAbnormalViewModel, ActivityRepostAgvabnormalBinding>() {

    private lateinit var agvAbnormalListBean: AgvAbnormalListBean

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        if (intent != null && intent.extras?.containsKey("AgvAbnormalListBean")!!) {
            agvAbnormalListBean =
                intent.extras?.getSerializable("AgvAbnormalListBean") as AgvAbnormalListBean
        }
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.btnPost clicks {
            if (v.rbResend.isChecked) {
                vm.dealAgvAbnormal(AgvAbnormalPostBean(agvAbnormalListBean.rwid,"","2"))
            } else {
                vm.dealAgvAbnormal(AgvAbnormalPostBean(agvAbnormalListBean.rwid,"","3"))
            }
        }
    }

    override fun initData() {

    }

    override fun initVM() {
        vm.dealResult.observe(this) {
            showShortToast(it[0].returntext)
            Event.getInstance().post(EventCode.REFRESH)
            finish()
        }
    }
}