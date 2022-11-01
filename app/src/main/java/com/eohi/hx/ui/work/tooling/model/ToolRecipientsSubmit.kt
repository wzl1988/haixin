package com.eohi.hx.ui.work.tooling.model

data class ToolRecipientsSubmit(
    val  ckyy : String,
    val  czrbh : String,
    val  gsh : String,
    val  sqrbh : String,
    val items: List<Item>
)
data class Item(
    val  dqszwz : String,
    var  gzbh: String
)