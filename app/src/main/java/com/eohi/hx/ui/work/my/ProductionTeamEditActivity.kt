package com.eohi.hx.ui.work.my

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.App
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityProductionTeamEditBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.my.adapter.ProductionAddPersonAdapter
import com.eohi.hx.ui.work.my.model.AddxzSubmitModel
import com.eohi.hx.ui.work.my.model.PersonData
import com.eohi.hx.ui.work.my.model.ProductionPersonModel
import com.eohi.hx.ui.work.my.model.UpdatepProcuctModel
import com.eohi.hx.ui.work.my.viewmodel.MyViewModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.ListDialog
import com.eohi.hx.widget.clicks

class ProductionTeamEditActivity : BaseActivity<MyViewModel,ActivityProductionTeamEditBinding>() {
    private lateinit var mAdapter: ProductionAddPersonAdapter
    private  lateinit var list : ArrayList<ProductionPersonModel>
    var type=""
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.tvAddPerson.clicks {
            vm.getCylist(companyNo)
        }
        v.btnSubmit.clicks {
            if(v.etXzmc.text.isEmpty()){
                showShortToast("小组名称不能为空")
                return@clicks
            }
            val listitem = ArrayList<PersonData>()
            for(i in list.indices){
                if(list[i].fpxs.isNullOrEmpty()){
                    list[i].fpxs="0"
                }
                var item = PersonData(list[i].fpxs,list[i].YGBH)
                listitem.add(item)
            }

            if(type == "update"){
                vm.updateScxz(UpdatepProcuctModel( intent.getStringExtra("code"),v.etXzmc.text.toString(),listitem))
            }else{
                vm.addScxz(AddxzSubmitModel(accout,v.etXzmc.text.toString(),listitem))
            }

        }
    }

    override fun initData() {
        type = intent.getStringExtra("Type")
        if(type == "update"){
            v.etXzmc.setText(intent.getStringExtra("name"))
            vm.getGroupMember(intent.getStringExtra("code"))
        }
        list = ArrayList()
        mAdapter = ProductionAddPersonAdapter(mContext,list)
        mAdapter.setDetele {
            list.removeAt(it)
            mAdapter.notifyDataSetChanged()
            v.tvBds.text ="小组绑定人员数："+list.size
        }
        mAdapter.onEditListener { i, s ->
            list[i].fpxs = s
        }
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = mAdapter
        }
    }

    override fun initVM() {
        vm.cylist.observe(this, Observer {
            if(it.isNotEmpty()){
                val strlist = ArrayList<String>()
                it.forEach {
                    strlist.add(it.YGBH+" "+it.XM)
                }
                val dialog =   ListDialog(this, "添加成员", strlist, object : ListDialog.MyListener {
                    override fun refreshActivity(position: Int) {
                        var isexit = true
                        for (i  in list.indices){
                           if(list[i].YGBH == it[position].YGBH){
                               isexit= false
                               break
                           }
                        }
                        if(isexit){
                            list.add(it[position])
                            mAdapter.notifyDataSetChanged()
                            v.tvBds.text ="小组绑定人员数："+list.size
                        }else{
                            showShortToast("成员已存在")
                        }

                    }
                })
                dialog.show()
            }
        })
        vm.addResult.observe(this, Observer {
            list.clear()
            mAdapter.notifyDataSetChanged()
            v.tvBds.text ="小组绑定人员数："+list.size
            showShortToast("提交成功")
            App.post(EventMessage(EventCode.REFRESH))
        })

        vm.groupMembers.observe(this, Observer {
            if(it.isNotEmpty()){
                list.clear()
                it.forEach {
                    var model = ProductionPersonModel("","","","",it.ZYXM,it.ZYID,"",it.FPXS.toString())
                    list.add(model)
                }
                mAdapter.notifyDataSetChanged()
            }
        })

    }
}