package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.BrandListAdapter
import com.car.bolang.bean.*
import com.car.bolang.common.BaseActivity
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_brand_list.*
import kotlinx.android.synthetic.main.activity_common_head.*

class BrandListActivity  : BaseActivity() , OnItemClickListener {


    private var mAdapter: BrandListAdapter?=null
    private var mDataList:MutableList<BrandRecord> =ArrayList()


    companion object {
        fun  startAction(context: Context){
            val intent= Intent(context,BrandListActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_brand_list
    }

    override fun init() {
        tvTitle.text="品牌列表"
        getBrandList()
        mAdapter= BrandListAdapter(this,mDataList)
        rvGoodList.adapter=mAdapter
        mAdapter?.setonItemClickListenr(this)
        rvGoodList.layoutManager= GridLayoutManager(this,2)
        ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun initData() {

    }

    private fun getBrandList(){
        mLoadingDialog?.show()
        val params = HashMap<String,String>()
        params["name"] = ""
        params["pageNum"] ="1"
        params["pageSize"] = "200"

        NetHelpUtils.okgoGet(this, UrlProtocol.BRAND_LIST,params, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz","result${result}")
                mLoadingDialog?.dismiss()
                goToRecharge(this@BrandListActivity)
                result?.let {
                    val bean=GsonUtil.GsonToBean(result,BrandListVO::class.java)
                    if (bean.data.records.isNotEmpty()) {
                        Log.e("zzzz","size:${bean.data.records.size}")
                        mDataList.addAll(bean.data.records)
                        mAdapter?.notifyDataSetChanged()
                    }else{
                        rvGoodList.visibility=View.GONE
                    }

                }?:{
                    rvGoodList.visibility=View.GONE
                }.invoke()

            }

            override fun onError(code: Int, errorMsg: String?) {
                ToastUtils.toastShort(this@BrandListActivity,"请求失败，请重试")
                mLoadingDialog?.dismiss()
            }
        })
    }

    override fun onItemClick(view: View?, position: Int) {
//        ProductDetailActivity.startAction(this,mDataList[position])
        val good=Goods(mDataList[position].id,mDataList[position].img,mDataList[position].name)
        ProductListActivity.startAction(this,good)
    }
}