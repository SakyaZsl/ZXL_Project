package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.GoodListAdapter
import com.car.bolang.bean.*
import com.car.bolang.common.BaseActivity
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ShareConstants
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity:BaseActivity() ,OnItemClickListener{


    private var dataList:MutableList<Record> =ArrayList()
    private var mAdapter:GoodListAdapter?=null


    companion object {
        fun startAction(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }


    override fun setLayoutResID(): Int {
        return R.layout.activity_search
    }



    override fun init() {
        tvCancel.setOnClickListener {
            finish()
        }

        etSearchContent.setOnEditorActionListener(object :TextView.OnEditorActionListener{

            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH)) {//如果是搜索按钮
                    if(etSearchContent.text.isEmpty()){
                        ToastUtils.toastShort(this@SearchActivity,"请输入搜索内容")
                        return true
                    }
                    SearchResultActivity.startAction(this@SearchActivity,etSearchContent.text.toString())
                }
                return false
            }

        })
        mAdapter= GoodListAdapter(dataList)
        mAdapter?.setonItemClickListener(this)
        rvGoodList.adapter=mAdapter
        rvGoodList.layoutManager=LinearLayoutManager(this)
    }

    override fun initData() {
    }

    fun search(){
        mLoadingDialog?.show()
        val params = HashMap<String,String>()
        params["name"] = etSearchContent.text.toString()
        params["brandId"] = ""
        params["pageNum"] ="1"
        params["pageSize"] = "100"

        NetHelpUtils.okgoGet(this, UrlProtocol.GOOD_LIST,params, object :
            HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                goToRecharge(this@SearchActivity)
                mLoadingDialog?.dismiss()
                val bean=GsonUtil.GsonToBean(result,GoodListVO::class.java)
                if (bean.data.records.isNotEmpty()) {
                    dataList.clear()
                    dataList.addAll(bean.data.records)
                    mAdapter?.notifyDataSetChanged()
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                ToastUtils.toastShort(this@SearchActivity,"请求失败，请重试")
                mLoadingDialog?.dismiss()
            }
        })
    }


    override fun onItemClick(view: View?, position: Int) {
        ProductDetailActivity.startAction(this,dataList[position])
    }
}