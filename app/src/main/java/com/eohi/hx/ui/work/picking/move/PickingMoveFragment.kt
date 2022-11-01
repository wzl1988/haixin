package com.eohi.hx.ui.work.picking.move

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.eohi.hx.App
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentPickingMoveBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/13 14:20
 */
class PickingMoveFragment : BaseFragment<PickingMoveViewModel, FragmentPickingMoveBinding>() {
    private var username by Preference("username", "")
    var startCkh: String? = null
    var startkwh: String? = null
    var endCkh: String? = null
    var endKwh: String? = null
    var type = ""
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.cklist.observe(this) {
            if (!it.isNullOrEmpty()) {
                startCkh = it[0].ckh
                startkwh = it[0].kwh
            }
        }
        vm.endcklist.observe(this) {
            if (!it.isNullOrEmpty()) {
                endCkh = it[0].ckh
                endKwh = it[0].kwh
            }
        }
        vm.itemlist.observe(this) {
            try {
                if (!it.isNullOrEmpty()) {
                    v.tvWph.text = it[0].wph
                    v.tvWpmc.text = it[0].wpmc
                    v.tvGg.text = it[0].gg
                    v.etZysl.text = Editable.Factory.getInstance().newEditable(it[0].sl.toString())
                    v.etFzsl.setText(it[0].fzsl.toString())
                    v.etFzdw.text = it[0].fzjldw
                    val item = ItemInfo()
                    item.tmh = it[0].tmh
                    item.FSL = it[0].fzsl.toString()
                    item.GGMS = it[0].gg
                    item.WPMC = it[0].wpmc
                    item.wph = it[0].wph
                    item.ZSL = it[0].sl.toString()
                    item.fckh = startCkh
                    item.fkwh = startkwh
                    item.ckh = endCkh
                    item.kwh = endKwh
                    item.ZDW = it[0].fzjldw
                    if ("agvxsfh" == type) {
                        item.fhtzdh = v.etGdh.text.toString()
                    } else {
                        item.gdh = v.etGdh.text.toString()
                    }
                    App.post(EventMessage(EventCode.DATA, "", "", 0, item))
                }
            } catch (e: Exception) {

            }

        }

    }

    override fun initView() {
        v.etZysl.addTextChangedListener(object : TextWatcher {
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

    override fun initClick() {
    }

    override fun initData() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data
        if ("work" == type) {
            v.etGdh.visibility = View.GONE
            v.tvGdhText.visibility = View.GONE
        } else {
            v.etGdh.visibility = View.VISIBLE
            v.tvGdhText.visibility = View.VISIBLE
            if ("agvxsfh" == type)
                v.tvGdhText.text = "发货通知单号"
        }

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
                    v.etStartPoint.isFocused -> {
                        v.etStartPoint.text = Editable.Factory.getInstance().newEditable(result)
                        vm.getCklist(result)
                        v.etEndPoint.requestFocus()
                    }
                    v.etEndPoint.isFocused -> {
                        v.etEndPoint.text = Editable.Factory.getInstance().newEditable(result)
                        vm.getEndList(result)
                        if (v.etGdh.visibility == View.VISIBLE) {
                            v.etGdh.requestFocus()
                        } else {
                            v.etTmh.requestFocus()
                        }

                    }
                    v.etGdh.isFocused -> {
                        v.etGdh.setText(result)
                        v.etTmh.requestFocus()
                    }
                    v.etTmh.isFocused -> {
                        v.etTmh.text = Editable.Factory.getInstance().newEditable(result)
                        if (null != startCkh) {
                            vm.getItemInfo(result, startCkh!!, startkwh!!)
                        } else {
                            ToastUtil.showToast(mContext, "请先扫描站点码")
                        }

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