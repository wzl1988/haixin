package com.eohi.hx.ui.work.my

import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityProcessingUnitBinding
import com.eohi.hx.ui.work.my.adapter.ProcessingUnitAdapter
import com.eohi.hx.ui.work.my.model.MyProcessingUnitModel
import com.eohi.hx.ui.work.my.viewmodel.MyViewModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.DialogSelectList
import com.eohi.hx.widget.clicks

//我的生产单员
class ProcessingUnitActivity : BaseActivity<MyViewModel, ActivityProcessingUnitBinding>() {
    private lateinit var list: ArrayList<MyProcessingUnitModel>
    private lateinit var madapter: ProcessingUnitAdapter
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.tvAdd.clicks {
            vm.getAllgjdy(accout, companyNo)
        }
    }

    override fun initData() {
        list = ArrayList()
        madapter = ProcessingUnitAdapter(this, list)
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = madapter
        }
        madapter.itemLongClick { view, i ->
            val popup = PopupMenu(mContext, view) //第二个参数是绑定的那个view
            //获取菜单填充器
            val inflater = popup.menuInflater
            //填充菜单
            inflater.inflate(R.menu.listmenu, popup.menu)
            //绑定菜单项的点击事件
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navigation_practice -> {
                        vm.deteleJgdy(list[i].GSH,list[i].SCXBH,list[i].CZRID)
                        list.removeAt(i)
                        v.tvJgdys.text ="当前绑定加工单元数："+list.size
                        madapter?.notifyDataSetChanged()

                    }
                }
                false
            }
            popup.show()

        }
        vm.getMyjgdyList(accout, 100, 1)

    }


    override fun initVM() {
        vm.unitlist.observe(this, Observer {
            if(it.isNotEmpty()){
                v.tvJgdys.text ="当前绑定加工单元数："+it.size
                list.clear()
                list.addAll(it)
                madapter.notifyDataSetChanged()
            }
        })

        vm.alljgdy.observe(this, Observer {list->
            if(list.isNotEmpty()){
                val strlist = ArrayList<String>()
                list.onEach {
                    strlist.add(it.SCXBH+" "+it.SCXMC)
                }
                val dialog= DialogSelectList(mContext,mContext,"加工单元",strlist)
                dialog.onItemClick {
                    dialog.dismiss()
                    vm.addJgdy(list[it].gsh,list[it].SCXBH, accout)
                }
                dialog.show()
            }
        })

        vm.addjgdy.observe(this, Observer {
            showShortToast("添加成功")
            vm.getMyjgdyList(accout, 100, 1)
        })

        vm.deteleJgdy.observe(this, Observer {
            showShortToast("删除成功")
        })
    }

}