package com.eohi.hx.ui.work.model

class FileUploadResult {
    var code = 0
    var msg: String? = null
    var data: DataBean? = null

    open class DataBean {
        var total = 0
        var list: String? = null
    }
}
