package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.GoodsGridAdapter
import com.car.bolang.bean.GoodsVO
import com.car.bolang.common.BaseActivity
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.util.Constants
import com.car.bolang.util.InitUtils
import kotlinx.android.synthetic.main.activity_choose_goods.*
import kotlinx.android.synthetic.main.activity_common_head.*

class ChooseGoodsActivity :BaseActivity(),OnItemClickListener {


    companion object {
        fun  startAction(context: Context,isGet:Boolean){
            val intent= Intent(context,ChooseGoodsActivity::class.java)
            intent.putExtra(Constants.GOOD_NAME,isGet)
            context.startActivity(intent)
        }
    }

    private var list:MutableList<GoodsVO> =ArrayList()
    private var isGet=false
    private var adapter:GoodsGridAdapter?=null
    override fun setLayoutResID(): Int {
        return R.layout.activity_choose_goods
    }

    override fun init() {
        tvTitle.text="取物品"
        isGet=intent.getBooleanExtra(Constants.GOOD_NAME,false)
        if(!isGet){
            tvTitle.text="还物品"
        }
        ivBack.setOnClickListener {
            finish()
        }
        list.addAll(InitUtils.initGoodList())
        adapter= GoodsGridAdapter(list)
        adapter?.setonItemClickListener(this)
        rvGoodList.adapter=adapter
        rvGoodList.layoutManager=GridLayoutManager(this,3)


    }

    override fun initData() {
    }

    override fun onItemClick(view: View?, position: Int) {
        if(isGet){
            RegisterGoodsActivity.startAction(this,position)
            return
        }
        GiveBackActivity.startAction(this,position)


    }
}