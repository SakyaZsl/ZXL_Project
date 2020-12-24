package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.ProductListAdapter
import com.car.bolang.bean.GoodListVO
import com.car.bolang.bean.Goods
import com.car.bolang.bean.Record
import com.car.bolang.common.BaseActivity
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.Constants
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_brand_list.*
import kotlinx.android.synthetic.main.activity_common_head.*

class ProductListActivity :BaseActivity() ,OnItemClickListener{


    private var mGoods:Goods?=null
    private var mAdapter:ProductListAdapter ?=null
    private var mDataList:MutableList<Record> =ArrayList()


    companion object {
        fun  startAction(context: Context,name:Goods){
            val intent= Intent(context,ProductListActivity::class.java)
            intent.putExtra(Constants.GOOD_NAME,name)
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_brand_list
    }

    override fun init() {
        mGoods= intent.getSerializableExtra(Constants.GOOD_NAME) as Goods?
        tvTitle.text=mGoods?.name
        getGoodList()
        mAdapter= ProductListAdapter(this,mDataList)
        rvGoodList.adapter=mAdapter
        mAdapter?.setonItemClickListener(this)
        rvGoodList.layoutManager=GridLayoutManager(this,2)
        ivBack.setOnClickListener {
            onBackPressed()
        }


    }

    override fun initData() {

    }

    private fun getGoodList(){
        mLoadingDialog?.show()
        val params = HashMap<String,String>()
        params["name"] = ""
        params["brandId"] = mGoods?.id.toString()
        params["pageNum"] ="1"
        params["pageSize"] = "100"

        NetHelpUtils.okgoGet(this, UrlProtocol.GOOD_LIST,params, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz","result${result}")
                mLoadingDialog?.dismiss()
                val bean= GsonUtil.GsonToBean(result, GoodListVO::class.java)
                if (bean.data.records.isNotEmpty()) {
                    mDataList.addAll(bean.data.records)
                    mAdapter?.notifyDataSetChanged()
                }else{
                    rvGoodList.visibility=View.GONE
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                ToastUtils.toastShort(this@ProductListActivity,"请求失败，请重试")
                mLoadingDialog?.dismiss()
            }
        })
    }

    override fun onItemClick(view: View?, position: Int) {
        ProductDetailActivity.startAction(this,mDataList[position])
    }
}