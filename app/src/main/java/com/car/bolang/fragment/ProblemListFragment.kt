package com.car.bolang.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.car.bolang.R
import com.car.bolang.activity.FeedbackActivity
import com.car.bolang.activity.QuestionDetailActivity
import com.car.bolang.activity.RechargeActivity
import com.car.bolang.adapter.MessageAdapter
import com.car.bolang.bean.ProblemListVO
import com.car.bolang.bean.ProblemRecord
import com.car.bolang.bean.SmartModelVo
import com.car.bolang.common.BaseActivity
import com.car.bolang.common.BaseFragment
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.InitUtils
import com.car.bolang.util.PreferencesUtil
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_problem_list.*
import java.util.HashMap

class ProblemListFragment  : BaseFragment() , OnItemClickListener {

    private var messageAdapter: MessageAdapter?=null
    private var dataList:MutableList<SmartModelVo> =ArrayList()

    override fun getLayoutId(): Int {
        return R.layout.activity_problem_list
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun initData() {
        ivBack.visibility=View.GONE
        tvTitle.text="智能模板"
        tvMore.text="添加模板"
        tvMore.visibility= View.VISIBLE
        tvMore.setOnClickListener {
            FeedbackActivity.startAction(activity!!)
        }
        dataList.addAll(InitUtils.initModeList())
        messageAdapter= MessageAdapter(dataList)
        rvProblemList.adapter=messageAdapter
        rvProblemList.layoutManager=LinearLayoutManager(activity)
        messageAdapter?.setonItemClickListener(this)
    }

    fun getProblemList(){
        val params = HashMap<String, String>()
        params["pageNum"]="1"
        params["pageSize"]="100"
        NetHelpUtils.okgoGet(activity, UrlProtocol.PROBLEM_LIST, params, object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                if(PreferencesUtil.getInstance().isExpire){
                    RechargeActivity.startAction(activity!!)
                    return
                }
                (activity as BaseActivity).mLoadingDialog?.dismiss()
                Log.e("zzzz","getProblemList${result}")
                result?.let {
                    val bean=GsonUtil.GsonToBean(it, ProblemListVO::class.java)
                    bean.data?.let {data->
                        if (data.records.isNotEmpty()) {
                            dataList.clear()
                            messageAdapter?.notifyDataSetChanged()
                        }else{
                            changeViewNoData()
                        }
                    }
                }?:{
                    ToastUtils.toastShort(activity!!, getString(R.string.network_error))
                }.invoke()
            }

            override fun onError(code: Int, errorMsg: String?) {
                (activity as BaseActivity).mLoadingDialog?.dismiss()
                ToastUtils.toastShort(activity!!, getString(R.string.network_error))
            }
        })
    }

    private fun  changeViewNoData(){
        rvProblemList.visibility=View.GONE
    }


    override fun onItemClick(view: View?, position: Int) {
//        QuestionDetailActivity.startAction(activity!!,dataList[position])
    }

}