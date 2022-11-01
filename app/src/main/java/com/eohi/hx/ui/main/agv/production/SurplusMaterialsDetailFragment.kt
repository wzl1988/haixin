package com.eohi.hx.ui.main.agv.production

import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentPickingMoveDetailBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.ui.work.adapter.InItemlistAdapter
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.Data
import com.eohi.hx.ui.work.model.InstorageModel
import com.eohi.hx.ui.work.purchasein.InstorageDetailViewModel
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.view.DialogAgv
import com.eohi.hx.view.DialogAutoEnd
import com.eohi.hx.view.DialogStartToEnd
import com.eohi.hx.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/16 15:41
 */
class SurplusMaterialsDetailFragment :
    BaseFragment<InstorageDetailViewModel, FragmentPickingMoveDetailBinding>() {
    private var accout by Preference("userid", "")
    private var username by Preference("username", "")
    private var adpater: InItemlistAdapter? = null
    private var listdata: ArrayList<ItemInfo>? = null
    var agvtype = ""
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {

        vm.mrkresult.observe(this) {
            if (it.code == 1000) {
                vm.subAgvType(agvtype)
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        }

        vm.agvtype.observe(this) {
            if (it.code == 1000) {
                var title = ""
                when (agvtype) {
                    "022" -> {
                        title = "产线余料退至退料存储区"
                    }
                    "023" -> {
                        title = "上线不合格材料退至上线不合格区"
                    }
                }

                val type = it.data!!.list!![0]
                if (type.QDLX == 1 && type.ZDLX == 0) { //启点自动分配
                    val dialog = DialogAgv(mContext, activity, "产线余料退至退料存储区")
                    dialog.onOkClick {
                        dialog.dismiss()
                        val agvsub = AgvSubmitModel(
                            companyNo, accout,
                            agvtype, "", it, "产线余料退至退料存储区", "", 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    dialog.show()
                } else if (type.QDLX == 0 && type.ZDLX == 1) {
                    val autoEndDialog = DialogAutoEnd(mContext, activity, "产线余料退至退料存储区")
                    autoEndDialog.onOkClick {
                        autoEndDialog.dismiss()
                        val agvsub = AgvSubmitModel(
                            companyNo, accout,
                            agvtype, it, "", "产线余料退至退料存储区", insmodel!!.swh, 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    autoEndDialog.show()
                } else if (type.QDLX == 0 && type.ZDLX == 0) {
                    val dialog = DialogStartToEnd(mContext, activity, "产线余料退至退料存储区")
                    dialog.onOkClick { s, s2 ->
                        dialog.dismiss()
                        val agvsub = AgvSubmitModel(
                            companyNo, accout,
                            agvtype, s, s2, "产线余料退至退料存储区", insmodel!!.swh, 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    dialog.show()
                }
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }

        }

        vm.agvresult.observe(this) {
            if (it.code == 1000) {
                ToastUtil.showToast(mContext, it.msg)
                listdata?.clear()
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        }
    }

    override fun initView() {
        listdata = ArrayList()
        adpater = InItemlistAdapter(mContext, listdata!!)
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
                    }
                }
                false
            }
            popup.show()

        }
    }

    var insmodel: InstorageModel? = null
    override fun initClick() {
        v.brnOk.clicks {
            if (listdata!!.isEmpty())
                return@clicks
            val mlistData = ArrayList<Data>()
            for (i in listdata!!.indices) {
                val data = Data(listdata!![i].FSL, listdata!![i].tmh, listdata!![i].ZSL)
                mlistData.add(data)
            }

            insmodel = InstorageModel(
                mlistData, username, "",
                accout, agvtype, companyNo, listdata!![0].ckh!!,
                listdata!![0].kwh!!, "", "", "", "", "", listdata!![0].gdh!!
            )
            vm.getOrderNoAgvSubmit("TMCGRK", insmodel!!)

        }
    }

    override fun initData() {
        v.brnOk.text = "确认退料"
    }

    override fun lazyLoadData() {
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.DATA) {
            val item = msg.obj as ItemInfo
            var isexit = false
            for (index in listdata!!.indices) {
                if (item.tmh == listdata!![index].tmh)
                    isexit = true
            }
            if (!isexit) {
                listdata?.add(item)
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, "条码已存在！")
            }

        }
    }

}