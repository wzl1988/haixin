package com.eohi.hx.ui.main.agv.alarmlog

import android.animation.ValueAnimator
import android.app.Activity
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.eohi.hx.R
import com.eohi.hx.base.BaseAdapter
import com.eohi.hx.databinding.LayoutAlarmlogItemBinding
import com.eohi.hx.ui.main.model.AlarmlogModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/2/21 14:37
 */
class AlarmlogAdapter(mContext: Activity, list: ArrayList<AlarmlogModel>) :BaseAdapter<LayoutAlarmlogItemBinding,AlarmlogModel>(mContext,list){
    override fun convert(v: LayoutAlarmlogItemBinding, t: AlarmlogModel, position: Int) {
        v.tvSbbh.text  = t.deviceNum
        v.tvSbxlh.text = t.deviceName
        v.tvQyid.text = t.areaId.toString()
        v.tvBjwz.text = t.channelDeviceId
        v.tvRwid.text = t.RWID
        v.tvRzsj.text = t.RZSJ
        v.tvBjms.text = t.alarmDesc
        v.tvCljy.text = t.channelName

        if(t.alarmReadFlag==0){
            v.viewPoint.visibility = View.VISIBLE
        }else{
            v.viewPoint.visibility = View.GONE
        }

        val width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        val height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        v.llDetail.measure(width, height)
//        v.tvBjms.measure(width, height)
//        v.tvCljy.measure(width, height)
        val mOriengHeight = v.llDetail.measuredHeight

        Log.e("mOriengHeight", "mOriengHeight=$mOriengHeight")

        v.tvDetail.setOnClickListener {
            var isopen = v.llDetail.isVisible
            var anim: ValueAnimator? = null
            if (isopen) {
                v.ivArrow.setImageResource(R.drawable.arrow_down)
                v.llDetail.visibility = View.GONE
                isopen = false
//                anim = ValueAnimator.ofInt(mOriengHeight, 0)
            } else {
                isopen = true
                v.llDetail.visibility = View.VISIBLE
                v.ivArrow.setImageResource(R.drawable.arrow_up)
//                anim = ValueAnimator.ofInt(0, mOriengHeight)
            }
//            anim.addUpdateListener {
//                v.llDetail.layoutParams.height = it.animatedValue as Int
//                v.llDetail.requestLayout()
//            }
//            anim.addListener(object : Animator.AnimatorListener {
//                override fun onAnimationStart(animation: Animator?) {
//                    v.llDetail.visibility = View.VISIBLE
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    if (isopen) {
//                        v.ivArrow.setImageResource(R.drawable.arrow_up)
//                        v.llDetail.visibility = View.VISIBLE
//                    } else {
//                        v.ivArrow.setImageResource(R.drawable.arrow_down)
//                        v.llDetail.visibility = View.GONE
//                    }
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                }
//
//                override fun onAnimationRepeat(animation: Animator?) {
//                }
//
//            })
//            anim.duration = 200
//            anim.start()
            if(t.alarmReadFlag==0){
                read?.let {
                    it(position)
                }
            }

        }
    }

    var read:((Int)->Unit)? = null

    fun setReadClick(read:((Int)->Unit)?){
        this.read = read
    }

}