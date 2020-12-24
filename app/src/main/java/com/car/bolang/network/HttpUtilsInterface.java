package com.car.bolang.network;

public interface HttpUtilsInterface {
    void onSuccess(String result);
    void onError(int code, String errorMsg);
}
