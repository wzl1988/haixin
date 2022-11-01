package com.eohi.hx.ui.main.agv

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.ArrayMap
import com.eohi.hx.App
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentAgvInstorageBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/2 20:56
 */
class AgvInFragment : BaseFragment<AgvInViewModel, FragmentAgvInstorageBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.iteminfo.observe(this) {
            if (null != it) {
                val item = it[0]
                v.tvCgdh.text = item.CGDDH
                v.tvWph.text = item.wph
                v.tvWpmc.text = item.WPMC
                v.tvGg.text = item.GGMS
                v.tvDw.text = item.ZDW
                v.tvNumber.text = item.ZSL
                item.tmh = v.etTmh.text.toString()
                v.etFzsl.text = item.fsl.toString()
                v.tvFzdw.text = item.fdw
                v.tvPh.text = item.ph
                v.tvKhwph.text = item.khwph
                v.tvRwdh.text = item.scrwdh
                v.tvCx.text = item.cxh
                if (v.etFzsl.text.isEmpty()) {
                    item.FSL = "0"
                } else {
                    item.FSL = v.etFzsl.text.toString()
                }

                if (v.etTmh.text.isNotEmpty()) {
                    App.post(EventMessage(EventCode.DATA, "", "", 0, item))
                }

            }
        }
    }

    override fun initView() {
    }

    override fun initClick() {
    }

    override fun initData() {
    }


    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        requireActivity().registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION && isVisibleToUser) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.etTmh.setText(result)
                val map = ArrayMap<String?, String?>()
                map["Barcode"] = result
                map["gsh"] = companyNo
                vm.getItemInfo(map)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        requireActivity().unregisterReceiver(scanReceiver)
        super.onPause()
    }


    override fun lazyLoadData() {

    }
}