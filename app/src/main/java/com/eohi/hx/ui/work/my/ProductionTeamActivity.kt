package com.eohi.hx.ui.work.my

import android.content.Intent
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseActivity
import com.eohi.hx.databinding.ActivityProductionTeamBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.work.my.adapter.ProductionTeamAdapter
import com.eohi.hx.ui.work.my.model.MyProductionTeamModel
import com.eohi.hx.ui.work.my.viewmodel.MyViewModel
import com.eohi.hx.utils.Extensions.asColor
import com.eohi.hx.utils.Extensions.showShortToast
import com.eohi.hx.utils.StatusBarUtil
import com.eohi.hx.view.DialogAlert
import com.eohi.hx.widget.clicks

class ProductionTeamActivity : BaseActivity<MyViewModel, ActivityProductionTeamBinding>() {
    private lateinit var list: ArrayList<MyProductionTeamModel>
    private lateinit var madapter: ProductionTeamAdapter
    private lateinit var dialog: DialogAlert
    private var p = 0
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
        v.tvAdd.clicks {
            val intent = Intent(this, ProductionTeamEditActivity::class.java)
            intent.putExtra("Type", "add")
            startActivity(intent)
        }
    }

    override fun initData() {
        list = ArrayList()
        dialog = DialogAlert(mContext, "确认删除此生产小组？")
        madapter = ProductionTeamAdapter(this, list)
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = madapter
        }
        madapter.setDetele { position ->
            dialog.setOnOkClick {
                p = position
                vm.deteleTeam(list[position].XZBH)
            }
            dialog.show()
        }
        madapter.setUpdate {
            val intent = Intent(this, ProductionTeamEditActivity::class.java)
            intent.putExtra("Type", "update")
            intent.putExtra("name", list[it].xzmc)
            intent.putExtra("code", list[it].XZBH)
            startActivity(intent)
        }
        vm.getScxzList(accout, 100, 1)
    }

    override fun initVM() {
        vm.scxzlist.observe(this, Observer {
            if (it.isNotEmpty()) {
                v.tvTeam.text = "当前绑定小组数：" + it.size
                list.clear()
                list.addAll(it)
                madapter.notifyDataSetChanged()
            }
        })
        vm.deleteResult.observe(this, Observer {
            showShortToast("删除成功")
            list.removeAt(p)
            madapter.notifyDataSetChanged()
            v.tvTeam.text = "当前绑定小组数：" + list.size
        })
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.REFRESH) {
            vm.getScxzList(accout, 100, 1)
        }

    }

}