package com.car.bolang.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.car.bolang.R
import com.car.bolang.activity.CommonWebActivity
import com.car.bolang.activity.RechargeActivity
import com.car.bolang.adapter.NewsListAdapter
import com.car.bolang.bean.NewData
import com.car.bolang.bean.NewListVO
import com.car.bolang.common.BaseFragment
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.InitUtils
import com.car.bolang.util.PreferencesUtil
import kotlinx.android.synthetic.main.fragment_message.*
import java.util.HashMap

class NewsListFragment : BaseFragment(), OnItemClickListener {

    private var mAdapter: NewsListAdapter? = null
    private var mDataList: MutableList<NewData> = ArrayList()
    override fun getLayoutId(): Int {
        return R.layout.fragment_message
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        mAdapter = NewsListAdapter(activity!!, mDataList)
        mDataList.addAll(InitUtils.initNewsList())
        rvMsgList.adapter = mAdapter
        rvMsgList.layoutManager = LinearLayoutManager(activity)
        mAdapter?.setonItemClickListener(this)
//        getNewsList()

    }

    fun getNewsList() {
        val params = HashMap<String, String>()
        params["pageNum"] = "1"
        params["pageSize"] = "100"
        NetHelpUtils.okgoGet(activity, UrlProtocol.NEWS_LIST, params, object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                result?.let {
                    if(PreferencesUtil.getInstance().isExpire){
                        RechargeActivity.startAction(activity!!)
                        return
                    }
                    var newListVO = GsonUtil.GsonToBean(it, NewListVO::class.java)
                    newListVO?.data?.let { recordList ->
                        if (recordList.isNotEmpty()) {
                            mDataList.clear()
                            mDataList.addAll(recordList)
                            mAdapter?.notifyDataSetChanged()

                        } else {
                            changeViewNoData()
                        }
                    }
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                Log.e("zzzz", " getNewsList onError code:${code}errorMsg:${errorMsg}")
                changeViewNoData()
            }
        })
    }

    fun changeViewNoData() {
        tvNoNews.visibility = View.VISIBLE
        rvMsgList.visibility = View.GONE
    }


    override fun onItemClick(view: View?, position: Int) {
        val bean=mDataList[position]
        CommonWebActivity.startAction(bean.title,bean.path,activity!!)
    }

}