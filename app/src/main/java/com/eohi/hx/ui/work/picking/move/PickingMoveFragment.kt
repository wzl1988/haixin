package com.eohi.hx.ui.work.picking.move

import android.Manifest
import android.annotation.SuppressLint
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
import com.eohi.hx.databinding.FragmentPickingMoveBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.Extensions.trimStr
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.EasyPermissions

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
    @SuppressLint("ClickableViewAccessibility")
    override fun initClick() {
        v.etStartPoint.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etStartPoint.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etStartPoint.width
                    - v.etStartPoint.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(Constant.REQUEST_CODE_SCAN)
                }
                return false
            }
        })
        v.etEndPoint.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable =  v.etEndPoint.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x >  v.etEndPoint.width
                    - v.etEndPoint.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(Constant.REQUEST_CODE_SCAN_02)
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
                    - v.etTmh.paddingRight
                    - drawable.intrinsicWidth
                ) {
                    //具体操作
                    checkCameraPermissions(Constant.REQUEST_CODE_SCAN_03)
                }
                return false
            }
        })


    }



    private fun checkCameraPermissions(requestCode:Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(requireContext(), *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(requireContext(), R.anim.`in`, R.anim.out)
            val intent = Intent(requireContext(), CaptureActivity::class.java)
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_TITLE, "扫码")
            intent.putExtra(com.eohi.hx.utils.Constant.KEY_IS_CONTINUOUS, com.eohi.hx.utils.Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                requireActivity(),
                intent,
                requestCode,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), com.eohi.hx.utils.Constant.RC_CAMERA, *perms
            )
        }
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
        requireActivity().registerReceiver(scanReceiver, intentFilter)
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
        requireActivity().unregisterReceiver(scanReceiver)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            val result = data.getStringExtra(Intents.Scan.RESULT).trimStr()
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN->{
                    v.etStartPoint.text = Editable.Factory.getInstance().newEditable(result)
                    vm.getCklist(result)
                    v.etEndPoint.requestFocus()
                }
                Constant.REQUEST_CODE_SCAN_02->{
                    v.etEndPoint.text = Editable.Factory.getInstance().newEditable(result)
                    vm.getEndList(result)
                    if (v.etGdh.visibility == View.VISIBLE) {
                        v.etGdh.requestFocus()
                    } else {
                        v.etTmh.requestFocus()
                    }
                }
                Constant.REQUEST_CODE_SCAN_03->{
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