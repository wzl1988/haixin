package com.eohi.hx.ui.work.equipment.model

data class EquipmentCheckItem(
    val abnormalStateValue: Any,
    val checkmethod: String,
    val checkstandard: String,
    val equCode: String,
    val equName: String,
    val faultConditionValue: Any,
    val inspecProjectCode: String,
    val inspecProjectDescrip: String,
    val inspecProjectName: String,
    val reRaLowerLimit: Any,
    val reRaupLimit: Any,
    var djjg: String
)