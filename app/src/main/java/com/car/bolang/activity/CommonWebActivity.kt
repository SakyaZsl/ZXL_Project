package com.car.bolang.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.webkit.*
import com.car.bolang.R
import com.car.bolang.common.BaseActivity
import com.car.bolang.util.Constants
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_common_web.*

class CommonWebActivity : BaseActivity() {
    private var mTitle: String = ""
    private var mUrl: String = ""

    companion object {
        fun startAction(title: String, url: String, context: Context) {
            val intent = Intent(context, CommonWebActivity::class.java)
            intent.putExtra(Constants.WEB_TITLE, title)
            intent.putExtra(Constants.WEB_URL, url)
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_common_web

    }

    override fun init() {
        mTitle = intent.getStringExtra(Constants.WEB_TITLE)
        mUrl = intent.getStringExtra(Constants.WEB_URL)
        tvTitle.text=mTitle
        ivBack.setOnClickListener {
            finish()
        }

    }

    override fun initData() {
        initWebView()
        webDetail.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if(newProgress in 1..99){
                    mLoadingDialog?.show()
                }else{
                    mLoadingDialog?.dismiss()
                }

            }
        }
        webDetail.loadUrl(mUrl)

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        val webSettings = webDetail.getSettings()
        //支持缩放，默认为true。
        webSettings.setSupportZoom(false)
        //调整图片至适合webview的大小
        webSettings.useWideViewPort = true
        // 缩放至屏幕的大小
        webSettings.loadWithOverviewMode = true
        //设置默认编码
        webSettings.defaultTextEncodingName = "utf-8"
        //设置自动加载图片
        webSettings.loadsImagesAutomatically = true
        // 支持JS
        webSettings.setJavaScriptEnabled(true)
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = true
        webSettings.loadWithOverviewMode = true
        // 支持插件
        webSettings.pluginState = WebSettings.PluginState.ON
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
    }

}