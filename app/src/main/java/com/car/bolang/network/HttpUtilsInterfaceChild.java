package com.car.bolang.network;

import com.lzy.okgo.request.base.Request;

import java.io.File;


public interface HttpUtilsInterfaceChild extends HttpUtilsInterface {
    void onStart(Request request);
    void onSuccess(File file);
    void onError(int code, String errorMsg);
    void onAfter(String s, Exception e);
}
