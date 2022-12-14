package com.eohi.hx.ui.work.purchasein

import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.hx.R
import com.eohi.hx.base.BaseFragment
import com.eohi.hx.databinding.FragmentInstorageDetailBinding
import com.eohi.hx.event.EventCode
import com.eohi.hx.event.EventMessage
import com.eohi.hx.ui.main.model.ItemInfo
import com.eohi.hx.ui.work.adapter.InItemlistAdapter
import com.eohi.hx.ui.work.model.AgvSubmitModel
import com.eohi.hx.ui.work.model.Data
import com.eohi.hx.ui.work.model.InstorageModel
import com.eohi.hx.utils.Preference
import com.eohi.hx.utils.ToastUtil
import com.eohi.hx.view.DialogAgv
import com.eohi.hx.view.DialogAutoEnd
import com.eohi.hx.view.DialogStartToEnd

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/12 10:06
 */
class InstorageDetailFragment :
    BaseFragment<InstorageDetailViewModel, FragmentInstorageDetailBinding>() {
    private var accout by Preference<String>("userid", "")
    private var username by Preference("username", "")
    private var companyno by Preference("companyNo", "")
    private var adpater: InItemlistAdapter? = null
    private var listdata: ArrayList<ItemInfo>? = null
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {
        vm.wjresult.observe(this, Observer {
            if (it.code == 1000) {
                ToastUtil.showToast(mContext, it.msg)
                listdata?.clear()
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }

        })

        vm.mrkresult.observe(this, Observer {
            if (it.code == 1000) {
                vm.subAgvType("040")
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        })

        vm.agvtype.observe(this, Observer {
            if (it.code == 1000) {
                val type = it.data!!.list!![0]
                if (type.QDLX == 1 && type.ZDLX == 0) { //??????????????????
                    val dialog = DialogAgv(mContext, activity, "????????????")
                    dialog.onOkClick {
                        dialog.dismiss()
                        var agvsub = AgvSubmitModel(
                            companyno, accout,
                            "040", "", it, "????????????", "", 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    dialog.show()
                } else if (type.QDLX == 0 && type.ZDLX == 1) {
                    val autoEndDialog = DialogAutoEnd(mContext, activity, "????????????")
                    autoEndDialog.onOkClick {
                        autoEndDialog.dismiss()
                        var agvsub = AgvSubmitModel(
                            companyno, accout,
                            "040", it, "", "????????????", insmodel!!.swh, 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    autoEndDialog.show()
                } else if (type.QDLX == 0 && type.ZDLX == 0) {
                    val dialog = DialogStartToEnd(mContext, activity, "????????????")
                    dialog.onOkClick { s, s2 ->
                        dialog.dismiss()
                        var agvsub = AgvSubmitModel(
                            companyno, accout,
                            "040", s, s2, "????????????", insmodel!!.swh, 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    dialog.show()
                } else {

                }

            } else {
                ToastUtil.showToast(mContext, it.msg)
            }

        })

        vm.agvresult.observe(this, Observer {
            if (it.code == 1000) {
                ToastUtil.showToast(mContext, it.msg)
                listdata?.clear()
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        })

    }

    override fun initView() {
        listdata = ArrayList()
        adpater = InItemlistAdapter(mContext, listdata!!)
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = adpater
        }
        adpater?.itemLongClick { view, i ->
            val popup = PopupMenu(context!!, view) //?????????????????????????????????view
            //?????????????????????
            val inflater = popup.menuInflater
            //????????????
            inflater.inflate(R.menu.listmenu, popup.menu)
            //??????????????????????????????
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
        v.brnOk.setOnClickListener {
            if (listdata!!.isEmpty())
                return@setOnClickListener
            var mlistData = ArrayList<Data>()
            for (i in listdata!!.indices) {
                var data = Data(listdata!![i].FSL, listdata!![i].tmh, listdata!![i].ZSL)
                mlistData.add(data)
            }

            insmodel = InstorageModel(
                mlistData, username, "",
                accout, "040", companyno, listdata!![0].ckh!!,
                "", "", "", "", "", "", ""
            )

            if (listdata!![0].cktype == 2) { //?????????
                vm.getOrderNoAgvSubmit("TMCGRK", insmodel!!)

            } else if (listdata!![0].cktype == 1) { //????????????
                insmodel!!.tkwh = listdata!![0].kwh!!
                vm.getOrderNo("TMCGRK", insmodel!!)
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
                ToastUtil.showToast(mContext, "??????????????????")
            }

        }else if(msg.code == EventCode.REFRESH){
            for (index in listdata!!.indices ){
                if(msg.msg== listdata!![index].tmh){
                    listdata!![index].ZSL = msg.arg1
                    break
                }
            }
            adpater?.notifyDataSetChanged()
        }
    }


}