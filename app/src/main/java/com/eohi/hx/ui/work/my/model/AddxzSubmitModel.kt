package com.eohi.hx.ui.work.my.model

data class AddxzSubmitModel(
    val CJYH: String,
    val XZMC: String,
    val `data`: List<PersonData>
)

data class PersonData(
    val FPXS: String,
    val ZYID: String
)