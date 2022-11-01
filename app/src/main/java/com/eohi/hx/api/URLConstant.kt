package com.eohi.hx.api


class URLConstant {
    companion object {

        //122.51.182.66  192.168.126.2  192.168.0.26:3019 122.51.182.66:3019
        private const val BASE_URL_DEBUG: String = "http://122.51.182.66:3019/"
        private const val BASE_URL_RELEASE: String = "http://192.168.3.34:3001/"

        val BASE_URL: String = if (com.eohi.hx.App.DEBUG) BASE_URL_DEBUG else BASE_URL_RELEASE

    }
}