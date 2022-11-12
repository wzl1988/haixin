package com.eohi.hx.api

import android.util.ArrayMap
import com.eohi.hx.response.BaseResModel
import com.eohi.hx.ui.login.model.CompanyModel
import com.eohi.hx.ui.login.model.LoginModel
import com.eohi.hx.ui.main.agvmodel.*
import com.eohi.hx.ui.main.model.*
import com.eohi.hx.ui.main.model.WarehouseInfo
import com.eohi.hx.ui.work.equipment.model.*
import com.eohi.hx.ui.work.model.*
import com.eohi.hx.ui.work.my.model.*
import com.eohi.hx.ui.work.quality.rejects.model.GoodListItemModel
import com.eohi.hx.ui.work.quality.rejects.model.RejectsDetermineSubmitModel
import com.eohi.hx.ui.work.quality.rejects.model.RejectsListModel
import com.eohi.hx.ui.work.tooling.model.*
import com.eohi.hx.utils.retrofit.FatherList
import com.eohi.hx.utils.retrofit.ServerBaseResult
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/8 17:37
 */
interface ApiService {

    //登录
    @POST("api/do?method=AgvPointInfo_YHDL")
    suspend fun login(@Body login: LoginModel): LoginResultModel

    //获取公司列表
    @GET("api/do?method=Agv_PAD_HXGetGS")
    suspend fun getCompany(): BaseResModel<CompanyModel>

    //获取单个起点
    @GET("/api/do")
    suspend fun getAgvStart(@QueryMap map: ArrayMap<String, String>): BaseResModel<StartAgvLocation>

    //获取点位列表
    @GET("/api/do")
    suspend fun getAgvPointList(@QueryMap map: ArrayMap<String?, String?>): BaseResModel<StartAgvLocation>

    //添加任务  大华 172.18.101.10   116.62.121.214
    @POST("http://172.18.101.10:7000/ics/taskOrder/addTask")
    suspend fun addTask(@Body model: AddTaskModel): TaskResult

    //释放点位
    @POST("/api/do?method=AgvPointInfo_PointRelease")
    suspend fun releasePoint(@Body model: PointModel): BaseResModel<PointResult>

    //检查点位
    @POST("/api/do?method=AgvPointInfo_PointInspection")
    suspend fun checkPointStuas(@Body model: PointModel): BaseResModel<PointResult>

    //提交调度信息
    @POST("/api/do?method=AgvPointInfo_SubmitSchedule")
    suspend fun submitPointInfo(@Body model: SavePointInfo): BaseResModel<PointResult>

    //获取所有库位
    @GET("/api/do?method=AgvPointInfo_GetCKXX")
    suspend fun getWarehouseInfo(): BaseResModel<WarehouseInfo>


    //所有库位号
    @GET("/api/do?method=Agv_CXJL_HXGetKWXX")
    suspend fun getKwList(@Query("CKH") ckh: String?): BaseResModel<KwModel>

    //获取物品信息
    @GET("/api/do?method=AGVInWarehouseDetail_GetItemDetail")
    suspend fun getItemInfo(@QueryMap map: ArrayMap<String?, String?>): BaseResModel<ItemInfo>


    //获取入库单号
    //    @GET("http://192.168.3.115:8081/api/SystemBillNumFactory/GetBillNumber")
    //    Single<OrderNomodel>  getOrderNo();
    @GET("/api/do?method=AgvPointInfo_GetRKDDJH")
    suspend fun getOrderNo(): BaseResModel<OrderNumberModel>

    @GET("/api/do?method=Agv_HXGetDJH")
    suspend fun getOrderNo(@Query("DJMC") dj: String): BaseResModel<OrderNumberModel>


    //入库提交
//    @POST("api/do?method=AGVPurchaseCodeRelation_Add")
//    fun inStorage(@Body model: InStorageModel): BaseResModel<PointResult>

    @POST("api/do?method=agvrk_addtmswzjb")
    suspend fun inStorage(@Body model: InstorageModel): BaseResModel<SubmitResult>

    //退库提交
    @POST("api/do?method=agvrk_addtmswzjb_th")
    suspend fun tkSubmit(@Body model: TkSubmitModel): BaseResModel<SubmitResult>


    //根据事务类型获取起终点的类型
    @POST("api/do?method=Agv_CXJL_HXGetKQDZDLX")
    suspend fun getPointgetType(@Query("YWLXM") YWLXM: String): BaseResModel<AgvLocationType>

    //agv任务调用
    @POST("api/do?method=Agvdd_DAGV")
    suspend fun addAGV(@Body model: AgvSubmitModel): BaseResModel<SubmitResult>

    //agv开关
    @GET("api/do?method=app_agvzt_update")
    suspend fun setAgv(@Query("zt")zt:Int): BaseResModel<SubmitResult>

    //获取AGV当前状态
    @GET("api/do?method=app_agvzt_get")
    suspend fun getAgvStatus():BaseResModel<AgvStatusModel>

    //根据点位获取库位 仓库
    @GET("/api/do?method=Agv_CXJL_HXGetCKXX")
    suspend fun pointToGetCk(@Query("DW") dw: String): BaseResModel<PointgetCkModel>

    //移库,出库 物品信息
    @GET("/api/do?method=Agv_CXJL_HXGetKCSL")
    suspend fun getMoveItemInfo(
        @Query("tmh") tmh: String,
        @Query("ckh") ckh: String,
        @Query("kwh") kwh: String
    ): BaseResModel<PickingWpxxModel>

    //领料出库--添加gdh
    @GET("/api/do?method=Agv_BASS_GetClckTxm")
    suspend fun getOutItemInfo( @Query("tmh") tmh: String,
                                @Query("ckh") ckh: String,
                                @Query("kwh") kwh: String,
                                @Query("gdh")gdh:String):BaseResModel<PickingWpxxModel>


    //获取产线
    @GET("api/do?method=Agv_CXJL_HXGetcxxx&CXXX=")
    suspend fun getProlineList(@Query("gsh") gsh: String): BaseResModel<ProductionlineModel>

    //退库原因
    @GET("api/do?method=AGV_GetXSTHRKYYM")
    suspend fun getTkyy(@Query("swlx") swlx: String): BaseResModel<ReasonModel>

    //客户号
    @GET("/api/do?method=Agv_CXJL_HXGetKHXX")
    suspend fun getKhh(@Query("gsh") gsh: String, @Query("khh") khh: String): BaseResModel<KhModel>

    //释放点位
    @POST("api/do?method=AgvPointInfo_PointRelease")
    suspend fun releasePoint(@Body model: PointReleaseModel): BaseResModel<SubmitResult>

    //退库物品信息
    @GET("/api/do?method=app_get_sccltm")
    suspend fun getTkItemInfo(
        @Query("gsh") gsh: String,
        @Query("tmh") tmh: String
    ): BaseResModel<TkItemModel>


    //获取外协供应商
    @GET("api/do?method=commom_gysxx_get")
    suspend fun getWxgysList(
        @Query("gsh") gsh: String,
        @Query("gysh") gysh: String
    ): BaseResModel<SupplierModel>

    //获取外协待开工卡信息
    @GET("api/do?method=app_wx_lzk_get")
    suspend fun getWxinfobycard(@Query("cardno") cardno: String): BaseResModel<WxkgInfoModel>

    //获取外协待开工工序
    @GET("api/do?method=app_wx_wxgx_get")
    suspend fun getWxgxlist(@Query("cardno") cardno: String): BaseResModel<WxgxInfoModel>

    //外协开工提交
    @POST("api/do?method=app_wx_kg_add")
    suspend fun subMitKg(@Body model: WxkgSubmitModel): BaseResModel<SubmitResult>

    //完工提交
    @POST("api/do?method=app_wx_wgrk_add")
    suspend fun subMitWg(@Body model: WxkgSubmitModel): BaseResModel<SubmitResult>


    //获取外协待完工卡信息
    @GET("api/do?method=app_wx_drklzk_get")
    suspend fun getWgInfoByCard(@Query("cardno") cardno: String): BaseResModel<WxkgInfoModel>

    //材料拆卡原因
    @GET("api/do?method=app_scbg_ckyy_get")
    suspend fun getRemovalReason(): BaseResModel<RemovalReasonModel>

    //生产材料卡拆卡
    @GET("api/do?method=app_scbg_scclkxx_get")
    suspend fun getCkxx(
        @Query("clkno") clkno: String,
        @Query("userid") userid: String
    ): BaseResModel<MaterialCardInfoModel>

    //材料拆卡提交
    @POST("api/do?method=app_scbg_scclkck_add")
    suspend fun submitRemobal(@Body model: RemovalSubModel): BaseResModel<SubmitResult>

    //获取生产登记日期
    @GET("api/do?method=app_scbg_bcxx_get")
    suspend fun getEupScrq(): BaseResModel<EquDateModel>


    //生产完工流转卡信息
    @GET("api/do?method=app_scbg_lzkxx_get")
    suspend fun getLzkxx(
        @Query("cardno") cardno: String,
        @Query("userid") userid: String
    ): BaseResModel<TransferCardInfoModel>

    //生产工序
    @GET("api/do?method=app_scbg_lzkglgx_get_all")
    suspend fun getScgx(
        @Query("cardno") cardno: String,
        @Query("gxno") gxno: String
    ): BaseResModel<ProductionProcessesModel>


    //获取工序和责任工序
    @GET("api/do?method=app_scbg_lzkglgx_get_all")
    suspend fun getGx(
        @Query("cardno") cardno: String,
        @Query("gxno") gxno: String
    ):MygxModel

    //获取小红点数量
    @GET("api/do?method=Manage_equ_rwsl")
    suspend fun getPointCount(@Query("userid") barcode: String):BaseResModel<EquipNumModel>

    //设备
    @GET("api/do?method=app_scbg_sbxx_get")
    suspend fun getSbxx(
        @Query("companycode") companycode: String,
        @Query("userid") userid: String
    ): BaseResModel<EquipmentModel>

    @GET("api/do?&method=app_scbg_sbxx_all_get")
    suspend fun getNewSBxx(
        @Query("companycode") companycode: String,
        @Query("userid") userid: String,
        @Query("scxbh") scxbh: String
    ): BaseResModel<EquipmentsModel>

    //物品列表
    @GET("api/do?method=app_bpppd_bfwl_get")
    suspend fun getDisposalItemList(@Query("rwdh")rwdh:String):BaseResModel<GoodListItemModel>

    //不良品判定提交
    @POST("api/do?method=app.scgl.blppd.tj")
    suspend fun postRejects(@Body model: RejectsDetermineSubmitModel): BaseResModel<SubmitResult>


    //加工单员
    @GET("api/do?method=app_scbg_lzkjgdy_get")
    suspend fun getJgdy(
        @Query("cardno") cardno: String,
        @Query("gxno") gxno: String,
        @Query("equno") equno: String
    ): BaseResModel<ProcessingUnitModel>

    @GET("api/do?method=app_scbg_lzkjgdy_sb_get")
    suspend fun getJgdysb(
        @Query("cardno") cardno: String,
        @Query("gxno") gxno: String
    ): BaseResModel<ProcessingUnitModel>

    //关联工序
    @GET("api/do?method=app_scbg_lzkglgx_get")
    suspend fun getGlgx(
        @Query("cardno") cardno: String,
        @Query("gxno") gxno: String
    ): BaseResModel<AssociatedprocessModel>

    //获取生产人员信息
    @GET("api/do?method=app_scbg_ygxx_get")
    suspend fun getPersonnelInfo(
        @Query("gsh") gsh: String,
        @Query("username") username: String
    ): BaseResModel<PersonModel>


    //材料卡消耗
    @GET("api/do?method=app_scbg_scclkxx_get")
    suspend fun getMaterialInfo(
        @Query("clkno") clkno: String,
        @Query("userid") userid: String
    ): BaseResModel<MaterialModel>

    //生产登记提交
    @POST("api/do?method=app_scbg_scdj_wg_add")
    suspend fun submitScdj(@Body proCardScdjAddList: ProductionSubList): BaseResModel<SubmitResult>

    //生产班组
    @GET("api/do?&method=GKJ_HXGetSCXZ1")
    suspend fun getProductTeam(
        @Query("ZYXM") ZYXM: String,
        @Query("pageSize") pageSize: Int,
        @Query("pageIndex") pageIndex: Int
    ): BaseResModel<ProductionTeamModel>


    //开工流转卡扫码
    @GET("api/do?method=app_scbg_lzkxx_get")
    suspend fun getKgInfoByCard(@Query("cardno") cardno: String): BaseResModel<KgInfoModel>

    //获取个人设备列表
    @GET("api/do?method=app_scbg_sbxx_get")
    suspend fun getPersonalEquipments(
        @Query("companycode") companycode: String,
        @Query("userid") userid: String
    ): BaseResModel<EquipmentsModel>

    //获取所有设备列表
    @GET("api/do?method=app_scbg_sbxx_all_get")
    suspend fun getAllEquipments(@QueryMap hashMap: HashMap<String, String>): BaseResModel<EquipmentsModel>

    //获取加工单元信息——设备未选择
    @GET("api/do?method=app_scbg_lzkjgdy_sb_get")
    suspend fun getJgdyNoEquipment(@QueryMap hashMap: HashMap<String, String>): BaseResModel<JgdyModel>

    //获取加工单元信息——设备已选择
    @GET("api/do?method=app_scbg_lzkjgdy_get")
    suspend fun getJgdy(@QueryMap hashMap: HashMap<String, String>): BaseResModel<JgdyModel>

    //获取开工工序
    @GET("api/do?method=app_scbg_kggx_get")
    suspend fun getGx(@Query("cardno") cardno: String): BaseResModel<GxModel>

    //开工提交
    @POST("api/do?method=app_scbg_scdj_kg_add")
    suspend fun postKg(@Body kgPostModel: KgPostModel): BaseResModel<SubmitResult>

    //流转卡拆卡——获取流转卡信息
    @GET("api/do?method=app_scbg_lzkxx_get")
    suspend fun getLzkxx(@Query("cardno") cardno: String): BaseResModel<LzkxxModel>

    //流转卡详情信息
    @GET("api/do?method=app.scgl.lzkxq.get")
    suspend fun getLzkDetailInfo(@Query("lzkkh") lzkkh: String):BaseResModel<LzkDetailModel>

    //流转卡拆卡——提交
    @POST("api/do?method=app_scbg_lzkck_add")
    suspend fun postLzk(@Body lzkPostModel: LzkPostModel): BaseResModel<SubmitResult>

    //流转卡详情——材料消耗
    @GET("api/do?method=app_scbg_lzkxq_clxhxx")
    suspend fun getCLXH(@Query("cardno") cardno: String): BaseResModel<CLXHModel>

    //流转卡详情——子列表
    @GET("api/do?method=app_scbg_lzkxq_gxtlshq")
    suspend fun getSubList(@Query("cardno") cardno: String): BaseResModel<LZKSubListModel>

    //流转卡详情——工序信息
    @GET("api/do?method=app_scbg_lzkxq_gxxx")
    suspend fun getGXResult(@Query("cardno") cardno: String): BaseResModel<GxResultModel>

    //调库存栈板——根据条码号获取物品信息
    @GET("api/do?method=Agv_CXJL_HXgjtmqwmc")
    suspend fun getTransferWp(@Query("tmh") tmh: String): BaseResModel<TransferWpModel>

    //调库存栈板——根据物品号获取库位信息
    @GET("api/do?method=Agv_CXJL_HXgjwpzdkw")
    suspend fun getTransferKw(@Query("wph") wph: String): BaseResModel<TransferKwModel>

    //调库存栈板——根据库位号获取当前库位的物品信息
    @GET("api/do?method=Agv_CXJL_HXgjkwqwoxx")
    suspend fun getTransferKwWp(@Query("kwh") kwh: String): BaseResModel<TransferKwWpModel>

    /**********************************************************************************************/
    /*质量管理*/
    //上传图片
    @POST("apidefine/updatefile")
    suspend fun fileUpload(@Body file: MultipartBody): FileUploadResult

    //不良现象
    @GET("api/do?method=GKJ_HXGetBLXX")
    suspend fun getBlxx(): BaseResModel<BlxxBean>

    //不良原因
    @GET("api/do?method=GKJ_HXGetBLYY")
    suspend fun getBlyy():BaseResModel<BlyyBean>

    //来料检验——列表
    @GET("api/do?method=APP_QM_LLRWJYLB")
    suspend fun getIncomingList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<IncomingListModel>

    //来料检验——历史列表
    @GET("api/do?method=APP_QM_LLJYJGLB")
    suspend fun getIncomingHistoryList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<IncomingListModel>

    //来料检验——处理
    @GET("api/do?method=APP_QM_LLJY_JYJMXSXX")
    suspend fun getIncomingTaskDetail(@QueryMap hashMap: HashMap<String, String>): CommonDetailModel

    //来料检验——提交
    @POST("api/do?method=APP_LLJY_TJ")
    suspend fun submitInspection(@Body model: CommonPostModel): BaseResModel<SubmitResult>

    //来料检验——详情
    @GET("api/do?method=APP_QM_LLJYJGXQ")
    suspend fun getIncomingDetail(@QueryMap hashMap: HashMap<String, String>): CommonDetailModel

    //来料检验——删除
    @GET("api/do?method=APP_QM_LLJYJGSC")
    suspend fun deleteIncoming(@Query("rwbh") rwbh: String): BaseResModel<DeleteResult>

    //来料检验——修改
    @POST("api/do?method=APP_QM_LLJYJGXG")
    suspend fun modifyIncoming(@Body model: CommonPostModel): BaseResModel<SubmitResult>

    //检验类型
    @GET("api/do?method=hx_bass_getxmfl")
    suspend fun getJylx(@Query("flbm") flbm: String): BaseResModel<JylxBean>

    //首件检验——列表
    @GET("api/do?method=APP_QM_SJJYRWLB")
    suspend fun getFirstCheckList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<FirstCheckListResult>

    //首件检验——结果列表
    @GET("api/do?method=APP_QM_SJJYJGLB")
    suspend fun getFirstCheckResultList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<FirstCheckListResult>

    //首件检验——处理(任务编号)
    @GET("api/do?method=APP_QM_SJJY_CL_GDH")
    suspend fun getFirstCheckData(@QueryMap hashMap: HashMap<String, String>): CommonDetailModel

    //首件检验——提交
    @POST("api/do?method=APP_SJJY_TJ")
    suspend fun postFirstCheck(@Body commonPostModel: CommonPostModel): BaseResModel<SubmitResult>

    //首件检验——结果详情
    @GET("api/do?method=APP_QM_SJJYJGXQ")
    suspend fun firstCheckDetail(@QueryMap hashMap: HashMap<String, String>): CommonDetailModel

    //首件检验——删除
    @GET("api/do?method=APP_QM_SJJYJGSC")
    suspend fun deleteFirstCheck(@Query("rwbh") rwbh: String): BaseResModel<DeleteResult>

    //首件检验——修改
    @POST("api/do?method=APP_QM_SJJYJGXG")
    suspend fun modifyFirstCheck(@Body model: CommonPostModel): BaseResModel<SubmitResult>

    //过程巡检——结果列表
    @GET("api/do?method=APP_QM_GCXJ_CKLB")
    suspend fun getProcessList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<ProcessCheckListResult>

    //过程巡检——详情
    @GET("api/do?method=APP_QM_GCXJ_JGXQ")
    suspend fun getGCXJDetail(
        @Query("gsh") gsh: String,
        @Query("gdh") gdh: String,
        @Query("gxh") gxh: String,
        @Query("djh") djh: String
    ): CommonDetailModel

    //过程巡检——工单号列表
    @GET("api/do?method=APP_QM_GCXJ_GDHXZ")
    suspend fun getGDList(
        @Query("gsh") gsh: String
    ): BaseResModel<GDListBean>

    //过程巡检——产线
    @GET("api/do?method=APP_QM_GCXJ_CXXZ")
    suspend fun getGCCXList(@Query("gsh") gsh: String): BaseResModel<CxBean>

    //过程巡检——工序选择
    @GET("api/do?method=APP_QM_GCXJ_GXXZ")
    suspend fun getGXList(
        @Query("gsh") gsh: String,
        @Query("gdh") GDH: String
    ): BaseResModel<GXListBean>

    //过程巡检——根据工单号查询信息
    @GET("api/do?method=APP_QM_GCXJ_ZJXMXX")
    suspend fun getGDResult(
        @Query("gsh") gsh: String,
        @Query("gdh") GDH: String
    ): CommonDetailModel

    //过程巡检——提交
    @POST("api/do?method=APP_QM_GCXJ_TJ")
    suspend fun postGCXJ(@Body model: CommonPostModel): BaseResModel<SubmitResult>

    //过程巡检——删除
    @GET("api/do?method=APP_QM_GCXJ_JGSC")
    suspend fun deleteProcess(
        @Query("gdh") gdh: String,
        @Query("djh") jydh: String
    ): BaseResModel<DeleteResult>

    //过程巡检——修改
    @POST("api/do?method=APP_QM_GCXJ_JGXG")
    suspend fun modifyProcess(@Body model: CommonPostModel): BaseResModel<SubmitResult>

    //完工检查——结果列表
    @GET("api/do?method=APP_QM_WGJYJGLB")
    suspend fun getFinishCheckList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<FinishCheckListResult>

    //完工检验——详情
    @GET("api/do?method=APP_QM_WGJYJGXQ")
    suspend fun getFinishCheckDetail(
        @Query("gsh") gsh: String,
        @Query("gdh") gdh: String,
        @Query("djh") djh: String
    ): CommonDetailModel

    //完工检验——工单号选择
    @GET("api/do?method=APP_QM_WGJY_XZGD")
    suspend fun getFinishCheckGDH(@Query("gsh") gsh: String): BaseResModel<GDListBean>

    //完工检验——查询信息(条码号)
    @GET("api/do?method=APP_QM_WGJY_DCZD_TMH")
    suspend fun getFinishMessageTMH(
        @Query("gsh") gsh: String,
        @Query("tmh") tmh: String
    ): CommonDetailModel

    //完工检验——提交
    @POST("api/do?method=APP_WGJY_TJ")
    suspend fun postFinish(@Body commonPostModel: CommonPostModel): BaseResModel<SubmitResult>

    //完工检验——信息查询(工单号)
    @GET("api/do?method=APP_QM_WGJY_DCZD_GDH")
    suspend fun getFinishMessageGDH(
        @Query("gsh") gsh: String,
        @Query("gdh") gdh: String
    ): CommonDetailModel

    //完工检验——删除
    @GET("api/do?method=APP_QM_WGJYJGSC")
    suspend fun deleteFinishCheck(
        @Query("gdh") gdh: String,
        @Query("djh") jydh: String
    ): BaseResModel<DeleteResult>

    //完工检验——修改
    @POST("api/do?method=APP_QM_WGJYJGXG")
    suspend fun modifyFinishCheck(@Body commonPostModel: CommonPostModel): BaseResModel<SubmitResult>

    //发货检验——详情
    @GET("api/do?method=APP_QM_FHJYJGXQ")
    suspend fun getDeliveryDetail(
        @Query("gsh") gsh: String,
        @Query("gdh") gdh: String,
        @Query("djh") djh: String
    ): CommonDetailModel

    //发货检验——查询信息(条码号)
    @GET("api/do?method=APP_QM_FHJY_DCZD_TMH")
    suspend fun getDeliveryMessageTMH(
        @Query("gsh") gsh: String,
        @Query("tmh") tmh: String
    ): CommonDetailModel

    //发货检验——信息查询(工单号)
    @GET("api/do?method=APP_QM_FHJY_DCZD_GDH")
    suspend fun getDeliveryMessageGDH(
        @Query("gsh") gsh: String,
        @Query("gdh") gdh: String
    ): CommonDetailModel

    //发货检验——提交
    @POST("api/do?method=APP_FHJY_TJ")
    suspend fun postDelivery(@Body commonPostModel: CommonPostModel): BaseResModel<SubmitResult>

    //发货检验——结果列表
    @GET("api/do?method=APP_QM_FHJYJGLB")
    suspend fun getDeliveryList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<DeliveryCheckListResult>

    //发货检验——删除
    @GET("api/do?method=APP_QM_FHJYJGSC")
    suspend fun deleteDelivery(
        @Query("gdh") gdh: String,
        @Query("djh") jydh: String
    ): BaseResModel<DeleteResult>

    //发货检验——修改
    @POST("api/do?method=APP_QM_FHJYJGXG")
    suspend fun modifyDelivery(@Body commonPostModel: CommonPostModel): BaseResModel<SubmitResult>

    //不合格品登记
    //流转卡详情
    @GET("api/do?method=app_scbg_lzkxx_get")
    suspend fun getLzkDetail(@Query("cardno") cardno: String): BaseResModel<LzkDetailResult>

    //不良品登记列表
    @GET("api/do?method=app_scbg_djxx_get")
    suspend fun getRejectsList(@Query("userid") userid:String):BaseResModel<RejectsListModel>

    //不合格品提交
    @POST("api/do?method=app.scgl.bhgpdj.tj")
    suspend fun unQualifiedPost(@Body unQualifiedPostModel: UnQualifiedPostModel): BaseResModel<SubmitResult>

    /**********************************************************************************************/

    //工装领用-领用原因
    @GET("api/do?method=app_gzgl_ckyy_get")
    suspend fun getGzlyyy(): BaseResModel<ToolRecipientsReasonModel>

    //工装领用信息
    @GET("api/do?method=app_gzgl_gzxx_get")
    suspend fun getGzxx(
        @Query("gzbm") gzbm: String,
        @Query("gsh") gsh: String
    ): BaseResModel<ToolinfoModel>

    //工装领用提交
    @POST("api/do?method=app_gzgl_gzly_add")
    suspend fun submitToolRecipients(@Body model: ToolRecipientsSubmit): BaseResModel<SubmitResult>

    //工装领用申请人接口
    @GET("api/do?method=app_gzgl_ygxx_get")
    suspend fun getSqrList(
        @Query("gsh") gsh: String,
        @Query("ygxm") ygxm: String
    ): BaseResModel<ToolPersonInfoModel>

    //工装归还原因
    @GET("api/do?method=app_gzgl_rkyy_get")
    suspend fun getBackReanson(): BaseResModel<ToolingBackReason>

    //工装归还获取仓库号
    @GET("api/do?method=Agv_CXJL_HXGetGZKWXX")
    suspend fun getCkh(@Query("KWH") KWH: String): BaseResModel<ToolCkModel>

    //工装归还信息
    @GET("api/do?method=app_gzgl_rkgzxx_get")
    suspend fun getToolbackInfo(
        @Query("gzbm") gzbm: String,
        @Query("gsh") gsh: String
    ): BaseResModel<ToolinfoModel>

    //工装归还提交
    @POST("api/do?method=app_gzgl_gzrk_add")
    suspend fun submitToolback(@Body model: ToolBackSubmit): BaseResModel<SubmitResult>

    //工装交接-获取待交接工装
    @GET("api/do?method=app_gzgl_djjgzxx_get")
    suspend fun getToolHandover(
        @Query("gzbm") gzbm: String,
        @Query("gsh") gsh: String
    ): BaseResModel<ToolhandoverModel>

    //工装交接-提交
    @POST("api/do?method=app_gzgl_gzjj_add")
    suspend fun submitHandover(@Body model: ToolhandoverSubModel): BaseResModel<SubmitResult>

    //工装领用列表
    @GET("api/do?method=app_gzgl_ly_list")
    suspend fun getLyList(
        @Query("gsh") gsh: String,
        @Query("userid") userid: String
    ): BaseResModel<ToolRecipientsListItem>

    //我的生产小组
    @GET("api/do?&method=GKJ_HXGetSCXZ1")
    suspend fun getScxzList(
        @Query("ZYXM") ZYXM: String,
        @Query("pageSize") pageSize: Int,
        @Query("pageIndex") pageIndex: Int
    ): BaseResModel<MyProductionTeamModel>

    //我的加工单元
    @GET("api/do?&method=GKJ_HXGetJGDYYYM")
    suspend fun getMyjgdylist(
        @Query("RYXX") RYXX: String,
        @Query("pagesize") pageSize: Int,
        @Query("pagesindex") pageIndex: Int
    ): BaseResModel<MyProcessingUnitModel>

    //删除我的加工单元
    @GET("api/do?&method=GKJ_HXDELETESBXX")
    suspend fun deteleJgdy(
        @Query("GSH") GSH: String,
        @Query("JGDYBH") JGDYBH: String,
        @Query("CJR") CJR: String
    ): BaseResModel<SubmitResult>

    //添加加工单元
    @GET("api/do?&method=GKJ_HXUPDATESBXX")
    suspend fun addJgdy(
        @Query("GSH") GSH: String,
        @Query("JGDYBH") JGDYBH: String,
        @Query("CJR") CJR: String
    ): BaseResModel<SubmitResult>

    //所有加工单员
    @GET("api/do?&method=GKJ_GetJGDYBBHDQ")
    suspend fun getAlljgdy(
        @Query("RYXX") RYXX: String,
        @Query("GSH") GSH: String
    ): BaseResModel<AllProcessingUnitModel>

    //生产小组成员
    @GET("api/do?&method=Agv_PAD_HXGetYGXX1")
    suspend fun getSccyList(@Query("gsh") gsh: String): BaseResModel<ProductionPersonModel>

    //删除生产小组
    @GET("api/do?method=GKJ_HXDELETEZUXX")
    suspend fun deleteXZ(@Query("XZBH") XZBH: String): BaseResModel<SubmitResult>

    //添加生产小组
    @POST("api/do?method=GKJ_INSERTSCXZWHB")
    suspend fun addScxz(@Body model: AddxzSubmitModel): BaseResModel<SubmitResult>

    //修改生产小组
    @POST("api/do?method=GKJ_UPDATESCXZWHB")
    suspend fun updateScxz(@Body model: UpdatepProcuctModel): BaseResModel<SubmitResult>

    //获取小组成员
    @GET("api/do?method=GKJ_HXGetYGXX")
    suspend fun getGroupmember(@Query("XZXX") XZXX: String): BaseResModel<GroupMemberModel>

    //点检扫描
    @GET("api/do?method=Manage_equ_scan")
    fun getCheckList(@Query("barcode") barcode: String): Call<ServerBaseResult<FatherList<EquipmentCheck>>>

    //点检保存
    @POST("api/do?method=Manage_equ_dailycheck")
    fun setCheckData(@Body dc: DailyCheck): Call<ServerBaseResult<Any>>

    //故障确认列表
    @GET("api/do?method=Manage_equ_gettask_gzqr")
    fun setTaskFaultList(@Query("userId") barcode: String): Call<ServerBaseResult<FatherList<Fault>>>

    //故障确认提交
    @POST("api/do?method=Manage_equ_gettask_gzqradd")
    fun setFaultData(@Body fp: FaultPost): Call<ServerBaseResult<Any>>

    //设备维保列表
    @GET("api/do?method=Manage_equ_wb_gettask_SBLB")
    fun setTaskSBLBss(@Query("userid") barcode: String): Call<ServerBaseResult<FatherList<TaskSBLBss>>>

    //设备维保列表 搜索
    @GET("api/do?method=Manage_equ_wb_gettask_SBLBss")
    fun setTaskSBLBss(
        @Query("Userid") barcode: String,
        @Query("text") text: String
    ): Call<ServerBaseResult<FatherList<TaskSBLBss>>>

    //设备部件列表
    @GET("api/do?method=Manage_equ_wb_gettask")
    fun getTaskPartlist(
        @Query("userid") barcode: String,
        @Query("sbbh") text: String
    )
            : Call<ServerBaseResult<FatherList<TaskPartModel>>>

    //设备维保项目
    @GET("api/do?method=Manage_equ_wb_gettask_checkitem")
    fun getMaintenanceProject(
        @Query("bjewm") bjewm: String,
        @Query("sbbh") sbbh: String
    )
            : Call<ServerBaseResult<FatherList<MaintenannceProject>>>

    //设备维保提交
    @POST("api/do?method=Manage_equ_wb_scwbsjadd")
    fun submitMaintenance(@Body model: MaintenannceSubmitmodel): Call<ServerBaseResult<Any>>

    //设备故障类型
    @GET("api/do?method=Manage_equ_gzqr_type")
    fun setTaskFaultType(@Query("sbbh") sbbh: String): Call<ServerBaseResult<FatherList<FaultType>>>

    //故障报修-扫码
    @GET("api/do?method=Manage_eq_scan")
    fun setEquipment(@Query("barcode") barcode: String): Call<ServerBaseResult<FatherList<EquipmentCheck>>>

    //获取故障报修用户信息‘
    @GET("api/do?method=Manage_equ_fault_qr")
    fun getPersonInfo(
        @Query("gsh") gsh: String,
        @Query("sbbh") sbbh: String,
        @Query("csh") csh: String
    ): Call<ServerBaseResult<FatherList<PersonInfo>>>

    //维修 数据提交
    @POST("api/do?method=Manage_equ_gzbx_add")
    fun setFault(@Body dc: Fault2): Call<ServerBaseResult<Any>>

    //设备维修列表
    @GET("api/do?method=Manage_equ_fault_gettask")
    fun getEupMaintainlist(@Query("userid") userid: String): Call<ServerBaseResult<FatherList<MaintainListModel>>>

    //维修历史记录
    @GET("api/do?method=Manage_equ_fault_getchecklist")
    fun getEupMaintainHistory(@Query("wxdh") wxdh: String): Call<ServerBaseResult<FatherList<EquMaintainHistory>>>

    //签入
    @POST("api/do?method=Manage_equ_fault_checkin")
    fun checkInMaintain(@Body model: EquMainRecordModel): Call<ServerBaseResult<Any>>

    //签出
    @POST("api/do?method=Manage_equ_fault_checkout")
    fun checkoutMaintain(@Body model: EquMainRecordModel): Call<ServerBaseResult<Any>>

    //删除记录
    @POST("api/do?method=Manage_equ_fault_check_removetime")
    fun removeMaintain(@Body model: EquMainRecordModel): Call<ServerBaseResult<Any>>

    //维修完成提交
    @POST("api/do?method=Manage_equ_fault_add")
    fun equmaintainSubmit(@Body model: EquMainFinishModel): Call<ServerBaseResult<Any>>

    //设备维修详情
    @GET("api/do?method=Manage_equ_gzxlrwqr")
    fun getEquMaintainDetail(@Query("SWH") SWH: String): Call<ServerBaseResult<FatherList<EquMainDetailModel>>>

    //获取agv异常处理列表——异常
    @GET("api/do?method=Agv_CXJL_HXagvycxx")
    suspend fun getAgvAbnormalList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<AgvAbnormalListBean>

    //获取agv异常处理列表——所有
    @GET("api/do?method=Agv_CXJL_HXagvycxx1")
    suspend fun getAgvAbnormalAllList(@QueryMap hashMap: HashMap<String, String>): BaseResModel<AgvAbnormalListBean>

    //agv异常处理
    @POST("api/do?method=app_agvgzcl")
    suspend fun dealAgvAbnormal(@Body agvAbnormalPostBean: AgvAbnormalPostBean): BaseResModel<SubmitResult>

    //agv取消
    @GET("api/do?method=app_agv_rwqx")
    suspend fun cancel(@Query("rwid") rwid: String,@Query("userid") userid: String): BaseResModel<SubmitResult>

    //根据条码获取失败的AGV任务
    @GET("/api/do?method=AGV_YCCL_GetAGVRWID")
    suspend fun getFailAgvTask(@Query("tmh") tmh: String): BaseResModel<AgvFailTaskModel>

    //agv异常任务处理
    @POST("api/do?method=AGV_YCCL_QR")
    suspend fun submitCancelTask(@Body model: TaskCancelModel): BaseResModel<SubmitResult>

    //工装上下线 机台号获取设备
    @GET("api/do?method=Manage_eq_scan")
    suspend fun getToolEquipment(@Query("barcode") barcode: String): BaseResModel<EquipmentCheck>

    //机台号以上线的工装列表
    @GET("api/do?method=app.tm.online.list")
    suspend fun getToolOnlinelist(@Query("sbbh") sbbh: String): BaseResModel<ToolListModel>

    //工装下线
    @POST("api/do?method=app.tm.offline.one")
    suspend fun subMitToolOffline(@Body model: ToolOfflineSubmitmodel): BaseResModel<SubmitResult>

    //工装上线
    @POST("api/do?method=app.tm.online.submit")
    suspend fun submitToolOnline(@Body model: ToolOnlineSubmitModel): BaseResModel<SubmitResult>

    //AGV警报任务
    @GET("api/do?method=app_agv_alarm_get")
    suspend fun getAlarmLogList(
        @Query("begintime") begintime: String,
        @Query("endtime") endtime: String,
        @Query("code") code: String,
        @Query("isread") isread: String,
        @Query("userid") userid: String,
        @Query("pagesize") pagesize: Int,
        @Query("pageindex") pageindex: Int
    ):BaseResModel<AlarmlogModel>

    //阅读报警日志
    @POST("api/do?method=app_agv_alarm_read")
    suspend fun subReadLog(@Body body:AlarmReadSub):BaseResModel<SubmitResult>


}