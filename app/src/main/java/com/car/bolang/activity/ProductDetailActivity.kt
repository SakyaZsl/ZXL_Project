package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.media.Image
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.ImagePageAdapter
import com.car.bolang.adapter.ProductDetailAdapter
import com.car.bolang.bean.GoodData
import com.car.bolang.bean.GoodDetailVO
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
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_product_detail.*
import java.util.HashMap

class ProductDetailActivity:BaseActivity(),OnItemClickListener {


    private var mGood:Record?=null
    private var imgHeadList:MutableList<String> =ArrayList()
    private var imgDetailList:MutableList<String> =ArrayList()
    private var mHeadAdapter:ImagePageAdapter ?= null
    private var mDetailAdapter:ProductDetailAdapter ?= null

    companion object {
        fun startAction(context: Context,id: Record) {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra(Constants.GOOD_ID,id)
            context.startActivity(intent)
        }
    }

    override fun setLayoutResID(): Int {
        return R.layout.activity_product_detail
    }


    override fun init() {
        mGood= intent.getSerializableExtra(Constants.GOOD_ID) as Record?
        ivBack.setOnClickListener {
            finish()
        }
        tvTitle.text="详情"
        mHeadAdapter= ImagePageAdapter(imgHeadList)
        vpImageList.adapter=mHeadAdapter

        mDetailAdapter= ProductDetailAdapter(this,imgDetailList)
        mDetailAdapter?.setonItemClickListener(this)

        rvProductDetail.adapter=mDetailAdapter
        rvProductDetail.layoutManager=LinearLayoutManager(this)
        rvProductDetail.isNestedScrollingEnabled = false
    }

    override fun initData() {
        if (mGood == null) {
            return
        }
        getGoodById(mGood!!.id)
    }

    private  fun getGoodById(id:Int){
        mLoadingDialog?.show()
        val params = HashMap<String, String>()
        NetHelpUtils.okgoGet(this, UrlProtocol.
            GOOD_LIST+"/${id}", params, object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz","result${result}")
                mLoadingDialog?.dismiss()
                result?.let {
                    var goodDetailVO=GsonUtil.GsonToBean(it, GoodDetailVO::class.java)
                    goodDetailVO?.data?.let {data->
                        initView(data)
                    }
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                mLoadingDialog?.dismiss()
                ToastUtils.toastShort(this@ProductDetailActivity, getString(R.string.network_error))
            }
        })
    }

    fun  initView(data: GoodData){

        tvSubTitle.text=data.subTitle
        tvCarName.text=data.title
        tvCarDescription.text=data.describes


        data.rotationChart?.let {
            val list=it.split(",")
            if (list.isNotEmpty()) {
                for(i in list){
                    imgHeadList.add(UrlProtocol.URL_IMG+i)
                }
            }
        }
        data.img?.let {
            val list=it.split(",")
            if (list.isNotEmpty()) {
                for(i in list){
                    imgDetailList.add(UrlProtocol.URL_IMG+i)
                }
            }
        }
        mHeadAdapter?.notifyDataSetChanged()
        mDetailAdapter?.notifyDataSetChanged()

        tvCarPrice.text="${data.price}元"

    }

    override fun onItemClick(view: View?, position: Int) {
        ImageActivity.startAction(this,imgDetailList[position])
    }
}