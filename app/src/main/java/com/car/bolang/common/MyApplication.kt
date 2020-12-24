package com.car.bolang.common

import android.app.Application
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.car.bolang.util.Constants
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ShareConstants
import com.lzy.okgo.OkGo
import com.lzy.okgo.model.HttpHeaders
import com.lzy.okgo.model.HttpParams
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.net.Proxy


class MyApplication:Application() {
    companion object{
        const val WX_APP_SECRET="2f58c150296a4272aa12cc04bc0ff852"
    }

    override fun onCreate() {
        super.onCreate()
        PreferencesUtil.getInstance().init(this)
        initOkGO()
        initWX()
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }


    private fun initOkGO(){
        OkGo.getInstance().init(this)
        val client=OkGo.getInstance().okHttpClient.newBuilder()
        client?.let {
//            it.proxy(Proxy.NO_PROXY)
            OkGo.getInstance().okHttpClient=it.build()
        }
        val headers = HttpHeaders()
        headers.put("token", PreferencesUtil.getInstance().getParam(ShareConstants.KEY_TOKEN,"").toString())    //header不支持中文
        OkGo.getInstance().addCommonHeaders(headers)
    }

    fun  initWX(){
        val api=WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID)
        api.registerApp(Constants.WX_APP_ID)
    }
}