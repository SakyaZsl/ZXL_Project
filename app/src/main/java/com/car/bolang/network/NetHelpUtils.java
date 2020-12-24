package com.car.bolang.network;

import android.content.Context;
import android.util.Log;

import com.car.bolang.activity.RechargeActivity;
import com.car.bolang.util.PreferencesUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONObject;

import java.io.File;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class NetHelpUtils {

    public static final  MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    public static final int SUCCESS=0;

    public static void okgoGetString(Context context, String url, final HttpUtilsInterface httpUtilsInterface){
        OkGo.getInstance().<String>get(url)
                .tag(context)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.body());
                        }
                    }
                });
    }

    public static void okgoGet(Context context, String url, Map<String, String> params, final HttpUtilsInterface httpUtilsInterface){
        OkGo.getInstance().<String>get(url)
                .params(params)
                .tag(context)
                .headers("token", PreferencesUtil.getInstance().getToken())
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.body());
                        }
                    }
                });
    }


    private static OkHttpClient getUnsafeOkHttpClient(OkHttpClient okHttpClient) {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void okgoPost(Context context, String url, Map<String, String> params, final HttpUtilsInterface httpUtilsInterface){
        OkHttpClient okHttpClient= OkGo.getInstance().getOkHttpClient();
        OkGo.getInstance().<String>post(url)
                .params(params)
                .tag(context)
                .headers("token", PreferencesUtil.getInstance().getToken())
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.body());
                        }
                    }
                });
    }


    public static void okGoBodyPost(Context context, String url, String json, final HttpUtilsInterface httpUtilsInterface){
        RequestBody requestBody=RequestBody.create(JSON,json);
        OkHttpClient okHttpClient= OkGo.getInstance().getOkHttpClient();
        OkGo.getInstance().<String>post(url)
                .upRequestBody(requestBody)
                .headers("token", PreferencesUtil.getInstance().getToken())
                .tag(context)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.body());
                        }
                    }
                });
    }

    public static void okgoGetFile(Context context, String url, final HttpUtilsInterfaceChild httpUtilsInterface){
        OkHttpClient okHttpClient= OkGo.getInstance().getOkHttpClient();
        OkGo.getInstance().<File>get(url)
                .tag(context)
                .execute(new FileCallback() {
                    @Override
                    public void onSuccess(Response<File> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);

                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.message());
                        }
                    }
                });

    }

    public static void uploadJSON(Context context, String url, JSONObject jsonObject, final HttpUtilsInterface httpUtilsInterface ){
        OkGo.<String>post(url)
                .tag(context)
                .upJson(jsonObject)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.message());
                        }
                    }
                });

    }

    public static void uploadFile(Context context, String url, Map<String, String> params, byte[] bytes, final HttpUtilsInterface httpUtilsInterface ){
        OkGo.<String>post(url)
                .tag(context)
                .upBytes(bytes)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.message());
                        }
                    }
                });

    }


    public static void uploadFile(Context context, String url, Map<String, String> params, String str, File file, final HttpUtilsInterface httpUtilsInterface ){
        OkHttpClient okHttpClient= OkGo.getInstance().getOkHttpClient();
        OkGo.<String>post(url)
                .tag(context)
                .params(str,file)
                .headers("token", PreferencesUtil.getInstance().getToken())
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.message());
                        }
                    }
                });

    }



    public static void uploadFile(Context context, String url, String str, File file, final HttpUtilsInterface httpUtilsInterface ){
        OkGo.<String>post(url)
                .tag(context)
                .params(str,file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.message());
                        }
                    }
                });

    }

    public static void uploadFile(final Context context, String url, String str, List<File> files, final HttpUtilsInterface httpUtilsInterface ){
        OkGo.<String>post(url)
                .tag(context)
                .addFileParams(str,files)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.isSuccessful()){
                            if (httpUtilsInterface!=null){
                                httpUtilsInterface.onSuccess(response.body());
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        if (httpUtilsInterface!=null){
                            httpUtilsInterface.onError(response.code(),response.message());
                        }
                    }
                });


    }



}
