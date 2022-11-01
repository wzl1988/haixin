package com.eohi.hx.ui.main.agv.sales

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import com.eohi.hx.App
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentAgvSalesDeliveryBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.agv.pick.AgvSendingViewModel
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Preference

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/11 14:58
 */
class AgvSalesFragment : BaseFragment<AgvSendingViewModel, FragmentAgvSalesDeliveryBinding>() {
    var ckh = ""
    var kwh = ""
    private var username by Preference("username", "")
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.cklist.observe(this) {
            if (!it.isNullOrEmpty()) {
                ckh = it[0].ckh
                kwh = it[0].kwh
            }
        }
        vm.itemlist.observe(this) {
            if (!it.isNullOrEmpty()) {
                v.tvWph.text = it[0].wph
                v.tvWpmc.text = it[0].wpmc
                v.tvGg.text = it[0].gg
                v.tvNumber.text = Editable.Factory.getInstance().newEditable(it[0].sl.toString())
                v.etFzdw.text = it[0].fzjldw
                val item = ItemInfo()
                item.tmh = it[0].tmh
                item.FSL = it[0].fzsl.toString()
                item.GGMS = it[0].gg
                item.WPMC = it[0].wpmc
                item.wph = it[0].wph
                item.ZSL = it[0].sl.toString()
                item.ckh = ckh
                item.kwh = kwh
                item.fhtzdh = v.etTzd.text.toString()
                App.post(EventMessage(EventCode.DATA, "", "", 0, item))
            }
        }

    }

    override fun initView() {

    }

    override fun initClick() {
        v.etTzd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (v.etTmh.text.isNotEmpty()) {
                    App.post(
                        EventMessage(
                            EventCode.REFRESH,
                            v.etTmh.text.toString(),
                            s.toString(),
                            0,
                            null
                        )
                    )
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        v.etFzsl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (v.etTmh.text.isNotEmpty()) {
                    App.post(
                        EventMessage(
                            EventCode.FSLREFRESH,
                            v.etTmh.text.toString(),
                            s.toString(),
                            0,
                            null
                        )
                    )
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })
    }

    override fun initData() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data
    }

    override fun lazyLoadData() {

    }

    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        activity!!.registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION && isVisibleToUser) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                when {
                    v.etTzd.isFocused -> {
                        v.etTzd.setText(result)
                        v.etZdm.requestFocus()
                    }
                    v.etTmh.isFocused -> {
                        v.etTmh.setText(result)
                        vm.getItemInfo(result, ckh, kwh)
                    }
                    v.etZdm.isFocused -> {
                        vm.getCklist(v.etZdm.text.toString())
                        v.etZdm.setText(result)
                        v.etTmh.requestFocus()
                    }
                }

            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity!!.unregisterReceiver(scanReceiver)
        super.onPause()
    }

}