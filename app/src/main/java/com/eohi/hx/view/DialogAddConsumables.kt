package com.eohi.hx.view

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import com.eohi.hx.R
import com.eohi.hx.ui.work.model.MaterialModel
import com.eohi.hx.utils.Constant
import com.eohi.hx.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import kotlinx.android.synthetic.main.dialog_add_consumables.*
import kotlinx.android.synthetic.main.dialog_add_consumables.btn_ok
import kotlinx.android.synthetic.main.dialog_add_consumables.iv_close
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/7 17:05
 */
class DialogAddConsumables(context: Context, themeResId: Int=R.style.MyDialog, var activity: Activity) : Dialog(context, themeResId)   {
    var sedata:SetData? =null
    var onbtnClick:onBtnClick?=null
    var data: MaterialModel?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_consumables)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        init()
    }

    private fun init() {
        iv_close.clicks {
            dismiss()
        }

        btn_cancel.clicks {
            dismiss()
        }

        btn_ok.clicks {
            if(data !=null)
            onbtnClick?.onBtnClick(data!!)
        }

        btn_search.clicks{
            if(et_kh.text.isNotEmpty())
            sedata?.getData(et_kh.text.toString())
        }
        et_kh.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                // getCompoundDrawables获取是一个数组，数组0,1,2,3,对应着左，上，右，下 这4个位置的图片，如果没有就为null
                val drawable = et_kh.compoundDrawables[2]
                //如果不是按下事件，不再处理
                if (event?.action != MotionEvent.ACTION_DOWN) {
                    return false
                }
                if (event.x > et_kh.width
                    - et_kh.paddingRight
                    - drawable.intrinsicWidth
                ){
                    //具体操作
                    checkCameraPermissions()
                }
                return false
            }

        })

    }


    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(activity, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.`in`, R.anim.out)
            val intent = Intent(activity, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                activity,
                intent,
                Constant.REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                activity, activity.getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }


    private val SCANACTION = "com.android.server.scannerservice.broadcast.haixin"
    private var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                var str = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                et_kh.setText(str)
                sedata?.getData(str)
            }
        }
    }




    override fun onStart() {
        super.onStart()
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        activity?.registerReceiver(scanReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        //取消广播注册
        activity?.unregisterReceiver(scanReceiver)
    }

    fun update(model: MaterialModel){
        data = model
        tv_bzh.text =model.clbzh
        tv_wpmc.text = model.wpmc
        tv_sl.text = model.sysl.toString()
    }

    interface onBtnClick{
        fun onBtnClick(model: MaterialModel)
    }


    interface SetData{
        fun getData(str:String)
    }

    fun update(str:String){
        et_kh.setText(str)
        sedata?.getData(str)
    }


}