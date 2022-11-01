package com.eohi.hx.ui.main.agv.abnormal


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityAgvAbnormalBinding
import com.eohi.hx.ui.main.agvmodel.TaskCancelModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.widget.clicks

/*
* agv异常处理1——状态为7.任务失败，3.任务取消
* */
class AgvAbnormalActivity : BaseActivity<AgvAbnormalViewModel, ActivityAgvAbnormalBinding>() {
    private var lx = "成功"
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
    }

    override fun initClick() {
        v.btnPost.clicks {
            if (v.tvTaskId.text.isEmpty()) {
                showShortToast("任务ID不能为空！")
                return@clicks
            }
            vm.subMit(TaskCancelModel(lx, v.tvTaskId.text.toString(), accout))
        }
        v.btnCancel.clicks {
            finish()
        }
        v.ivBack.clicks { finish() }
    }

    override fun initData() {
        v.rg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_success -> {
                    lx = "成功"
                }
                R.id.rb_cancel -> {
                    lx = "取消"
                }
            }
        }
    }

    override fun initVM() {
        vm.iteminfo.observe(this) {
            if (it.isNotEmpty()) {
                v.tvTaskId.text = it[0].cjrid
                v.tvBusinessType.text = it[0].ywlx
                v.tvStartingPoint.text = it[0].qd
                v.tvEnd.text = it[0].zd
                v.tvTransactionNumber.text = it[0].swh
                v.tvCreator.text = it[0].cjrid
                v.tvCreationTime.text = it[0].cjsj

            }
        }

        vm.agvresult.observe(this) {
            showShortToast("提交成功！")
            finish()
        }
    }


    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.etTmh.setText(result)
                vm.getFialTask(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}