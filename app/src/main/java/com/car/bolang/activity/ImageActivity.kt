package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import com.car.bolang.R
import com.car.bolang.bean.Record
import com.car.bolang.common.BaseActivity
import com.car.bolang.util.Constants
import com.car.bolang.util.GlideUtils
import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity :BaseActivity(){
    private var mUrl=""

    companion object {
        fun startAction(context: Context, url: String) {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra(Constants.GOOD_ID,url)
            context.startActivity(intent)
        }
    }
    override fun setLayoutResID(): Int {
        return R.layout.activity_image
    }

    override fun init() {
        mUrl=intent.getStringExtra(Constants.GOOD_ID)
        GlideUtils.basisGlide(this,mUrl,null,ivShowDetail)
        ivBack.setOnClickListener { finish() }
    }

    override fun initData() {
    }
}