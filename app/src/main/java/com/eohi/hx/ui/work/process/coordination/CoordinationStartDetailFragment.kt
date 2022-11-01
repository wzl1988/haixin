package com.eohi.hx.ui.work.process.coordination

import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.base.BaseViewModel
import com.eohi.hx.databinding.FragmentInstorageDetailBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.adapter.WxItemlistAdapter
import com.eohi.hx.ui.work.model.WxkgInfoModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/18 10:25
 */
class CoordinationStartDetailFragment :BaseFragment<BaseViewModel, FragmentInstorageDetailBinding>() {
    private var adpater: WxItemlistAdapter? = null
    private var listdata :ArrayList<WxkgInfoModel>? =null
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {
        listdata = ArrayList()
        adpater = WxItemlistAdapter(mContext,listdata!!)
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = adpater
        }
        adpater?.itemLongClick { view, i ->
            val popup = PopupMenu(context!!, view) //第二个参数是绑定的那个view
            //获取菜单填充器
            val inflater = popup.menuInflater
            //填充菜单
            inflater.inflate(R.menu.listmenu, popup.menu)
            //绑定菜单项的点击事件
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navigation_practice -> {
                        listdata?.removeAt(i)
                        adpater?.notifyDataSetChanged()
                        (activity as CoordinationStartActivity).update(getCount(),itemSum())
                    }
                }
                false
            }
            popup.show()

        }
    }

    override fun initView() {
    }

    override fun initClick() {
    }

    override fun initData() {
        v.brnOk.visibility = View.GONE
    }

    override fun lazyLoadData() {
    }

    fun updateList(data:WxkgInfoModel){
        var isexit = true
        for (i in listdata!!.indices){
            if(data.lzkkh == listdata!![i].lzkkh){
                isexit = false
                break
            }
        }
        if(isexit){
            listdata?.add(data)
            adpater?.notifyDataSetChanged()
        }

    }

    fun getCount():Int{
        return listdata?.size?:0
    }

    fun itemSum():Int{
        if(null  == listdata)
            return 0
        var sum = 0
        listdata!!.forEach {
            sum += it.sybzs
        }
        return  sum
    }

    fun getList():ArrayList<WxkgInfoModel>?{
        return listdata
    }
    fun clear(){
        listdata?.clear()
        adpater?.notifyDataSetChanged()
    }

    override fun handleEvent(msg: EventMessage) {
        if(msg.code ==EventCode.REFRESH&& listdata!=null){
            for(i in listdata!!.indices){
                if(listdata!![i].lzkkh == msg.msg){
                    listdata!![i].gxh = msg.arg1
                    break
                }
            }
            adpater?.notifyDataSetChanged()
        }

    }


}