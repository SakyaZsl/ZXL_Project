package com.car.bolang.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment :Fragment(){

    protected var mActivity: Context?=null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity=context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(mActivity)
            .inflate(getLayoutId(), container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view,savedInstanceState)
        initData()
    }


    protected abstract fun getLayoutId(): Int

    /**
     * 该抽象方法就是 初始化view
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract fun initView(view: View, savedInstanceState: Bundle?)

    /**
     * 执行数据的加载
     */
    protected abstract fun initData()
}