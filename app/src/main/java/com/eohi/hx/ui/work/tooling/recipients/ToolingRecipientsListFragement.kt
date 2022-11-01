package com.eohi.hx.ui.work.tooling.recipients

import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentToolingRecipientslistBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.tooling.adapter.ToolingRecipientsAdapter
import com.eohi.hx.ui.work.tooling.model.Item
import com.eohi.hx.ui.work.tooling.model.ToolRecipientsSubmit
import com.eohi.hx.ui.work.tooling.model.ToolinfoModel
import com.eohi.hx.ui.work.tooling.viewmodel.ToolingRecipientsViewModel
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/12 10:08
 */
class ToolingRecipientsListFragement :
    BaseFragment<ToolingRecipientsViewModel, FragmentToolingRecipientslistBinding>() {
    private var adpater: ToolingRecipientsAdapter? = null
    private lateinit var listdata: ArrayList<ToolinfoModel>
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {
        vm.result.observe(this, Observer {
            listdata.clear()
            adpater?.notifyDataSetChanged()
            ToastUtil.showToast(mContext, "提交成功")
        })
    }

    override fun initView() {
        listdata = ArrayList()
        adpater = ToolingRecipientsAdapter(mContext, listdata)
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = adpater
        }
        adpater?.setOnTextChange { s, i ->
            listdata[i].dqszwz = s
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
                        listdata.removeAt(i)
                        adpater?.notifyDataSetChanged()
                    }
                }
                false
            }
            popup.show()

        }
    }

    override fun initClick() {
        v.btnSubmit.clicks {
            if (listdata.isNotEmpty()) {
                val items = ArrayList<Item>()
                for (i in listdata.indices) {
                    items.add(Item(listdata[i].dqszwz, listdata[i].gzbm))
                }
                val model = ToolRecipientsSubmit(
                    listdata[0].lyyy, accout, companyNo, listdata[0].sqrbh, items
                )
                vm.submit(model)
            }
        }
    }

    override fun initData() {

    }

    override fun lazyLoadData() {
    }


    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.DATA) {
            val item = msg.obj as ToolinfoModel
            var isexit = true
            for (i in listdata.indices) {
                if (listdata[i].gzbm == item.gzbm) {
                    isexit = false
                    break
                }
            }
            if (isexit) {
                listdata.add(item)
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, "工装编码已存在")
            }

        }

    }
}