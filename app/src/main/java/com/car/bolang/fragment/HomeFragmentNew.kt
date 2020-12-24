package com.car.bolang.fragment

import android.os.Bundle
import android.view.View
import com.car.bolang.R
import com.car.bolang.activity.ChooseGoodsActivity
import com.car.bolang.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_home_new.*

class HomeFragmentNew :BaseFragment(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_home_new
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

        llGetGoods.setOnClickListener {
            ChooseGoodsActivity.startAction(activity!!,true)
        }


        llBackGoods.setOnClickListener {
            ChooseGoodsActivity.startAction(activity!!,false)
        }

    }

    override fun initData() {
    }

}