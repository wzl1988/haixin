package com.eohi.hx.utils.retrofit;

public class ApiErrorModel {
    public static final int SERVER_ERROR = 0;//请求错误
    public static final int OTHER_ERROR = 1;//解析错误

    private int errorType;
    private int errorCode;
    private String errorMsg;

    public ApiErrorModel(int errorType, String errorMsg) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ApiErrorModel(int errorType, int errorCode, String errorMsg) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public boolean isOtherError() {
        return getErrorType() == OTHER_ERROR;
    }

    public int getErrorType() {
        return errorType;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
