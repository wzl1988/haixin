package com.eohi.hx.ui.work.picking.outbound

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentPickingOutboundBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.ui.main.model.KwModel
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/14 16:32
 */
class PickingOutboundFragment :
    BaseFragment<PickingOutboundViewModel, FragmentPickingOutboundBinding>() {
    private var username by Preference("username", "")
    private var ishardware = 0//是否是五金
    private var warehouseInfolist: ArrayList<WarehouseInfo>? = null
    private var cklist: ArrayList<String?> = ArrayList()
    private var mkwList: List<KwModel>? = null
    private var kwlist: ArrayList<String?> = ArrayList()
    override fun isNeedEventBus(): Boolean {
        return false
    }

    companion object {
        const val REQUEST_GDH = 0x001
        const val REQUEST_TMH = 0x002
        const val REQUEST_KWH = 0x003
    }


    override fun initVM() {
        vm.whlist.observe(this) {
            warehouseInfolist = it
            cklist.clear()
            for (i in warehouseInfolist!!.indices) {
                if (null != warehouseInfolist!![i].CKMC)
                    cklist.add(warehouseInfolist!![i].CKMC)
            }
            dialogList()
        }

        vm.kwlist.observe(this) {
            mkwList = it
            kwlist.clear()
            for (i in mkwList!!.indices) {
                if (null != mkwList!![i].kwmc)
                    kwlist.add(mkwList!![i].kwmc)
            }
//            dialogkwList();
        }

        vm.itemlist.observe(this) {
            if (!it.isNullOrEmpty()) {
                v.tvWph.text = it[0].wph
                v.tvWpmc.text = it[0].wpmc
                v.tvGg.text = it[0].gg
                v.etZysl.text = Editable.Factory.getInstance().newEditable(it[0].sl.toString())
                v.etFzdw.text = it[0].fzjldw
                val item = ItemInfo()
                item.cktype = ishardware //仓库类型
                item.tmh = it[0].tmh
                item.FSL = it[0].fzsl.toString()
                item.GGMS = it[0].gg
                item.WPMC = it[0].wpmc
                item.wph = it[0].wph
                item.ZSL = it[0].sl.toString()
                item.ckh = v.etCkh.text.toString()
                item.kwh = v.etKwh.text.toString()
                item.gdh = v.etGdh.text.toString()
                App.post(EventMessage(EventCode.DATA, "", "", 0, item))
            }
        }

    }

    override fun initView() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data

        v.etCkh.setOnClickListener {
            if (warehouseInfolist != null && warehouseInfolist!!.isNotEmpty()) {
                dialogList()
            } else {
                vm.getCklist()
            }
        }
        v.etKwh.setOnClickListener {
            if (mkwList != null && mkwList!!.isNotEmpty()) {
                dialogkwList()
            }
        }


    }


    private fun dialogList() {
        if (cklist.size == 0) return
        val items = cklist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etCkh.text = warehouseInfolist!![which].CKH
            if (null != warehouseInfolist) {
                val model = warehouseInfolist!![which]
                //是AGV
                if (model.SFAGVCK.equals("true")) {
                    ishardware = 2
                    v.etKwh.setText("自动分配库位")
                } else {
                    ishardware = 1
                    vm.getKwlist(model.CKH)
                }
            }
        }
        builder.create().show()
    }


    private fun dialogkwList() {
        if (kwlist.size == 0) return
        val items = kwlist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etKwh.setText(mkwList!![which].kwh)
        }
        builder.create().show()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initClick() {
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

        v.etGdh.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etGdh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etGdh.width
                    - v.etGdh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(REQUEST_GDH)
                }
                return false
            }

        })
        v.etTmh.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etTmh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etTmh.width
                    -  v.etTmh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(REQUEST_TMH)
                }
                return false
            }

        })
        v.etKwh.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etKwh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etKwh.width
                    -  v.etKwh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(REQUEST_KWH)
                }
                return false
            }

        })
    }

    override fun initData() {
    }

    override fun lazyLoadData() {
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
                if (0 == ishardware) {
                    ToastUtil.showToast(mContext, "请先选择仓库")
                    return
                }
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                if (v.etGdh.isFocused) {
                    v.etGdh.setText(result)
                    v.etTmh.isFocusable = true
                    v.etTmh.requestFocus()
                } else if (v.etTmh.isFocused) {
                    if (v.etGdh.text.isEmpty()) {
                        v.etGdh.requestFocus()
                        ToastUtil.showToast(mContext, "请先扫描工单号")
                        return
                    }
                    v.etTmh.setText(result)
                    vm.getItemInfo(
                        result,
                        v.etCkh.text.toString(),
                        v.etKwh.text.toString(),
                        v.etGdh.text.toString()
                    )
                } else if (v.etKwh.isFocused) {
                    v.etKwh.setText(result)
                    v.etGdh.requestFocus()
                }


            }
        }
    }

    override fun onPause() {
        //取消广播注册
        requireActivity().unregisterReceiver(scanReceiver)
        super.onPause()
    }


    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions(code: Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(requireActivity(), *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(
                    requireActivity(),
                    R.anim.`in`,
                    R.anim.out
                )
            val intent = Intent(requireActivity(), CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                requireActivity(),
                intent,
                code,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            val result = data?.getStringExtra(Intents.Scan.RESULT).toString().trim { it <= ' ' }
            when (requestCode) {
                REQUEST_GDH -> {
                    v.etGdh.setText(result)
                    v.etTmh.isFocusable = true
                    v.etTmh.requestFocus()
                }
                REQUEST_TMH -> {
                    if (v.etGdh.text.isEmpty()) {
                        v.etGdh.requestFocus()
                        ToastUtil.showToast(mContext, "请先扫描工单号")
                        return
                    }
                    v.etTmh.setText(result)
                    vm.getItemInfo(
                        result,
                        v.etCkh.text.toString(),
                        v.etKwh.text.toString(),
                        v.etGdh.text.toString()
                    )
                }
                REQUEST_KWH->{
                    v.etKwh.setText(result)
                    v.etGdh.requestFocus()
                }

            }
        }
    }

}