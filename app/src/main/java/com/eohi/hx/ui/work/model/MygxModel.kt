package com.eohi.hx.ui.work.model

data class MygxModel(
    val code: Int,
    val `data`: GXData,
    val msg: String
)

data class GXData(
    val list: ArrayList<ProductionProcessesModel>,
    val total: Int,
    val zrgx: ArrayList<ProductionProcessesModel>
)
