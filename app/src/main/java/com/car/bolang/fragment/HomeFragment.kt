package com.car.bolang.fragment

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.car.bolang.R
import com.car.bolang.activity.*
import com.car.bolang.adapter.CarInfoAdapter
import com.car.bolang.adapter.GoldDetailAdapter
import com.car.bolang.adapter.ImageBannerAdapter
import com.car.bolang.bean.BannerBean
import com.car.bolang.bean.HomePageVO
import com.car.bolang.common.BaseActivity
import com.car.bolang.common.BaseFragment
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ToastUtils
import com.youth.banner.indicator.CircleIndicator
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.HashMap

class HomeFragment : BaseFragment() {
    private var goldAdapter: GoldDetailAdapter? = null
    private var carAdapter: CarInfoAdapter? = null
    private var bannerAdapter:ImageBannerAdapter ?=null
    private var imageList:MutableList<String> =ArrayList()


    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }


    override fun initView(view: View, savedInstanceState: Bundle?) {
        bannerAdapter= ImageBannerAdapter(imageList)
        bannerAdapter?.setContext(activity!!)
        bannerHome.adapter=bannerAdapter
        bannerHome.indicator=CircleIndicator(activity)

    }

    override fun initData() {
        llSearch.setOnClickListener {
            activity?.let {
                SearchActivity.startAction(it)
            }
        }
        if (TextUtils.isEmpty(PreferencesUtil.getInstance().token)) {
            activity?.let {
                ToastUtils.toastShort(it, "您还未登录,请先登录")
                LoginActivity.startAction(it)
            }

            return
        }
        getHomePage()
        getBanner()
        tvAllBrand.setOnClickListener {
            BrandListActivity.startAction(activity!!)
        }
        refreshLayout.setOnRefreshListener {
            getHomePage()
        }

    }

    private fun getHomePage() {
        (activity as BaseActivity).mLoadingDialog?.show()
        NetHelpUtils.okgoGet(activity, UrlProtocol.HOME_PAGE, null, object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz"," getHomePage result${result}")
                (activity as BaseActivity).mLoadingDialog?.dismiss()
                refreshLayout.finishRefresh()
                result?.let {
                    var homePageVO = GsonUtil.GsonToBean(it, HomePageVO::class.java)
                    homePageVO?.data?.let { data ->
                        PreferencesUtil.getInstance().isExpire=data.isExpire
                        if(data.isExpire){
                            RechargeActivity.startAction(activity!!)
                            return
                        }
                        data.reaTime?.let {time->
                            tvGoldTime.text=time
                        }
                        //金价
                        data.goldPriceList?.let { goldList ->
                            if (goldList.isNotEmpty()) {
                                goldAdapter = GoldDetailAdapter(goldList)
                                rvGoldList.adapter = goldAdapter
                                rvGoldList.layoutManager = GridLayoutManager(activity, 2)
                                rvGoldList.isNestedScrollingEnabled = false
                            }
                        }
                        //汽车
                        data.goodsList?.let { goodList ->
                            if (goodList.isNotEmpty()) {
                                carAdapter = CarInfoAdapter(activity!!, goodList)
                                carAdapter?.setonItemClickListenr(object : OnItemClickListener {
                                    override fun onItemClick(view: View?, position: Int) {
                                        ProductListActivity.startAction(
                                            activity!!,
                                            goodList[position]
                                        )
                                    }
                                })
                                rvCarList.adapter = carAdapter
                                rvCarList.layoutManager = GridLayoutManager(activity, 3)
                                rvCarList.isNestedScrollingEnabled = false
                            }
                        }
                    }
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                (activity as BaseActivity).mLoadingDialog?.dismiss()
                refreshLayout.finishRefresh()
                ToastUtils.toastShort(activity!!, getString(R.string.network_error))
            }
        })
    }


    private fun getBanner(){
        val params = HashMap<String, String>()
        NetHelpUtils.okgoGet(activity, UrlProtocol.
            HOME_BANNER, params, object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                Log.e("zzzz","result${result}")
                result?.let {
                    val bean=GsonUtil.GsonToBean(it, BannerBean::class.java)
                    bean?.data?.let {list->
                        if (list.isNotEmpty()) {
                            val dataList:MutableList<String> =ArrayList()
                           for(i in list){
                               dataList.add(UrlProtocol.URL_IMG+i)
                           }
                            ivBanner.visibility=View.GONE
                            bannerHome.visibility=View.VISIBLE
                            imageList.addAll(dataList)
                            bannerAdapter?.notifyDataSetChanged()
                        }
                    }
                }
            }

            override fun onError(code: Int, errorMsg: String?) {
                (activity as BaseActivity).mLoadingDialog?.show()
                ToastUtils.toastShort(activity!!, getString(R.string.network_error))
            }
        })
    }

}