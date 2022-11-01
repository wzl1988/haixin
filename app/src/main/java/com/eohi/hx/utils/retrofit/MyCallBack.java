package com.eohi.hx.utils.retrofit;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MyCallBack<T> implements Callback<ServerBaseResult<T>> {

    public abstract void success(T t);

    public abstract void failure(ApiErrorModel apiErrorModel);

    @Override
    public void onResponse(Call<ServerBaseResult<T>> call, Response<ServerBaseResult<T>> response) {
        if (response.isSuccessful() && response.body() != null) {
            if (response.body().getCode() == 200) {
                success( response.body().getData());
            } else {
                failure(new ApiErrorModel(ApiErrorModel.OTHER_ERROR, response.body().getCode(), response.body().getMsg()));
            }
        } else {
            failure(new ApiErrorModel(ApiErrorModel.SERVER_ERROR, response.code(), "服务器在偷懒，请稍后重试"));
        }
    }

    @Override
    public void onFailure(Call<ServerBaseResult<T>> call, Throwable t) {

        ApiErrorModel apiErrorModel;
        if (t instanceof SocketTimeoutException) {//超时
            apiErrorModel = new ApiErrorModel(ApiErrorModel.SERVER_ERROR, "超时");
        } else if (t instanceof ConnectException) {//连接错误
            apiErrorModel = new ApiErrorModel(ApiErrorModel.SERVER_ERROR, "连接错误");
        } else if (t instanceof UnknownError) { //未找到主机
            apiErrorModel = new ApiErrorModel(ApiErrorModel.SERVER_ERROR, "未找到主机");
        } else {//其他错误
            apiErrorModel = new ApiErrorModel(ApiErrorModel.SERVER_ERROR, "未知错误");
        }
        failure(apiErrorModel);


    }
}
