package com.eohi.hx.ui.work.sales.delivery

import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentPackingOutboundDetailBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.ui.work.adapter.InfinishlistAdapter
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.Data
import com.eohi.hx.ui.work.model.InstorageModel
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.view.DialogAutoEnd

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/19 10:14
 */
class SalesDeliveryDetailFragment :
    BaseFragment<SalesDeliveryDetailViewModel, FragmentPackingOutboundDetailBinding>() {
    private var accout by Preference("userid", "")
    private var username by Preference("username", "")
    private var adpater: InfinishlistAdapter? = null
    private var listdata: ArrayList<ItemInfo>? = null
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {
        vm.mckresult.observe(this) {
            if (it.code == 1000) {
                val autoEndDialog = DialogAutoEnd(mContext, activity, "Agv调度")
                autoEndDialog.onOkClick {
                    var agvsub = AgvSubmitModel(
                        companyNo, accout,
                        "035", it, "", "销售发货出库", "", 0, ""
                    )
                    vm.agvTaskadd(agvsub)
                    autoEndDialog.dismiss()
                }
                autoEndDialog.show()

            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        }

        vm.agvresult.observe(this) {
            vm.dismissLoading()
            if (it.code == 1000) {
                ToastUtil.showToast(mContext, it.msg)
                listdata?.clear()
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        }

        vm.wjresult.observe(this) {
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
        adpater = InfinishlistAdapter(mContext, listdata!!)
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = adpater
        }
        adpater?.itemLongClick { view, i ->
            val popup = PopupMenu(requireContext(), view) //第二个参数是绑定的那个view
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

    override fun initClick() {
        v.brnOk.setOnClickListener {
            if (listdata!!.isEmpty())
                return@setOnClickListener
            val mlistData = ArrayList<Data>()
            for (i in listdata!!.indices) {
                val data = Data(listdata!![i].FSL, listdata!![i].tmh, listdata!![i].ZSL)
                mlistData.add(data)
            }
            val insmodel = InstorageModel(
                mlistData, username, "",
                accout, "035", companyNo, listdata!![0].ckh!!,
                listdata!![0].kwh!!, "", "", "", "", listdata!![0].fhtzdh!!, ""
            )

            if (listdata!![0].cktype == 2) { //非五金,AGV
                vm.getOrderNoAgvSubmit("TMXSFH", insmodel)
            } else if (listdata!![0].cktype == 1) { //五金仓库
                insmodel.tkwh = listdata!![0].kwh!!
                vm.submit("TMXSFH", insmodel)
            }

        }
    }

    override fun initData() {
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

        } else if (msg.code == EventCode.REFRESH) {
            for (index in listdata!!.indices) {
                if (msg.msg == listdata!![index].tmh) {
                    listdata!![index].fhtzdh = msg.arg1
                    break
                }
            }
            adpater?.notifyDataSetChanged()
        } else if (msg.code == EventCode.SLREFRESH) {
            for (index in listdata!!.indices) {
                if (msg.msg == listdata!![index].tmh) {
                    listdata!![index].ZSL = msg.arg1
                    break
                }
            }
            adpater?.notifyDataSetChanged()
        } else if (msg.code == EventCode.FSLREFRESH) {
            for (index in listdata!!.indices) {
                if (msg.msg == listdata!![index].tmh) {
                    listdata!![index].FSL = msg.arg1
                    break
                }
            }
            adpater?.notifyDataSetChanged()
        }
    }

}