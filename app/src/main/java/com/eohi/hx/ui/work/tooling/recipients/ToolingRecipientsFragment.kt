package com.eohi.hx.ui.work.tooling.recipients

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentToolingRecipientsBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.tooling.viewmodel.ToolingRecipientsViewModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.utils.DateUtil
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/12 9:55
 */
class ToolingRecipientsFragment :
    BaseFragment<ToolingRecipientsViewModel, FragmentToolingRecipientsBinding>() {
    private lateinit var personlist: ArrayList<String>
    private lateinit var personNum: ArrayList<String>
    private lateinit var dialogPerson: ListDialog
    var ygbh = accout
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.lyyylist.observe(this, Observer {
            val yylist = ArrayList<String>()
            it.forEach {
                yylist.add(it.ckyy)
            }
            showListPopulWindow(v.tvLyyy, yylist.toTypedArray())
        })
        vm.gzxx.observe(this, Observer {
            if (it.isNotEmpty()) {
                v.tvGg.text = it[0].gg
                v.tvCkh.text = it[0].ckh
                v.tvKwh.text = it[0].kwh
                v.tvDd.text = it[0].dqszwz
                it[0].slr = v.tvSlr.text.toString()
                it[0].lyyy = v.tvLyyy.text.toString()
                it[0].sqrbh = ygbh
                App.post(EventMessage(EventCode.DATA, "", "", 0, it[0]))
            }
        })

        vm.personlist.observe(this, Observer {
            if (it.isNotEmpty()) {
                personlist.clear()
                it.onEach {
                    personlist.add(it.xm)
                    personNum.add(it.ygbh)
                }
                dialogPerson.show()
                dialogPerson.refreshList(personlist)
            }
        })

    }

    override fun initView() {
    }

    override fun initClick() {
        v.rlLyyy.clicks {
            vm.getLyyy()
        }
        v.rlSql.clicks {
            vm.getPersonList(companyNo, "")
        }
        v.ivSacn.clicks {
            checkCameraPermissions()
        }
    }

    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(mContext, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(mContext, R.anim.`in`, R.anim.out)
            val intent = Intent(activity, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            activity?.let {
                ActivityCompat.startActivityForResult(
                    it,
                    intent,
                    Constant.REQUEST_CODE_SCAN,
                    optionsCompat.toBundle()
                )
            }
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    if (v.tvLyyy.text.isEmpty()) {
                        ToastUtil.showToast(mContext, "请先选择领用原因！")
                        return
                    }
                    v.tvGzbh.text = result
                    vm.getGzxx(result, companyNo)
                }
            }
        }
    }

    override fun initData() {
        v.tvSlr.text = username
        v.tvCzy.text = username
        v.tvSj.text = DateUtil.dataTime
        personlist = ArrayList()
        personNum = ArrayList()
        dialogPerson = ListDialog(mContext, "人员选择", personlist, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvSlr.text = personlist[position]
                ygbh = personNum[position]
            }
        })
        dialogPerson.onSearchClick {
            vm.getPersonList(companyNo, it)
        }
    }

    override fun lazyLoadData() {
    }


    private fun showListPopulWindow(mEditText: TextView, list: Array<String>) {
        val listPopupWindow = ListPopupWindow(mContext)
        listPopupWindow.setAdapter(
            ArrayAdapter(
                mContext,
                R.layout.pop_item,
                list
            )
        ) //用android内置布局，或设计自己的样式
        listPopupWindow.anchorView = mEditText //以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        )
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //设置项点击监听
            mEditText.setText(list[i]) //把选择的选项内容展示在EditText上
            listPopupWindow.dismiss() //如果已经选择了，隐藏起来
        }
        listPopupWindow.show() //把ListPopWindow展示出来
    }


    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        activity?.registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                if (v.tvLyyy.text.isEmpty()) {
                    ToastUtil.showToast(mContext, "请先选择领用原因！")
                    return
                }
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.tvGzbh.text = result
                vm.getGzxx(result, companyNo)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity?.unregisterReceiver(scanReceiver)
        super.onPause()
    }


}