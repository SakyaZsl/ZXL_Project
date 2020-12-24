package com.car.bolang.common

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.view.AnimAlertDialog

abstract class  BaseActivity :AppCompatActivity() {

    public var mLoadingDialog:AnimAlertDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        mLoadingDialog= AnimAlertDialog(this)
        setContentView(setLayoutResID())
        init()
        initData()
    }


    protected abstract fun setLayoutResID(): Int

    /**
     * 初使化数据
     */
    protected abstract fun init()

    protected abstract fun initData()

    fun goToRecharge(context:Context){
        if (PreferencesUtil.getInstance().isExpire) {
            goToRecharge(context)
            return
        }
    }

}