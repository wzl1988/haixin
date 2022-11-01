package com.eohi.hx.ui.work.tooling.model

data class ToolOnlineSubmitModel(
    val items: List<OnLineItem>,
    val sbbh: String,
    val userid: String
)
data class OnLineItem(
    val gzbh: String
)