package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.MessageAdapter
import com.car.bolang.bean.GoodDetailVO
import com.car.bolang.bean.ProblemListVO
import com.car.bolang.bean.ProblemRecord
import com.car.bolang.bean.SmartModelVo
import com.car.bolang.common.BaseActivity
import com.car.bolang.network.HttpUtilsInterface
import com.car.bolang.network.NetHelpUtils
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GsonUtil
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_problem_list.*
import java.util.HashMap

class ProblemListActivity :BaseActivity() {
    private var messageAdapter:MessageAdapter ?=null
    private var dataList:MutableList<SmartModelVo> =ArrayList()

    companion object{

        fun  startAction(context: Context){
            val intent= Intent(context,ProblemListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
    override fun setLayoutResID(): Int {
        return R.layout.activity_problem_list
    }

    override fun init() {
        ivBack.setOnClickListener { finish() }
        tvTitle.text="问题"
        tvMore.text="提问"
        tvMore.visibility= View.VISIBLE
        tvMore.setOnClickListener {
            FeedbackActivity.startAction(this)
        }

        messageAdapter= MessageAdapter(dataList)
        rvProblemList.adapter=messageAdapter
        rvProblemList.layoutManager=LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        getProblemList()
    }

    override fun initData() {

    }

    private fun getProblemList(){
        mLoadingDialog?.show()
        val params = HashMap<String, String>()
        params["pageNum"]="1"
        params["pageSize"]="100"
        NetHelpUtils.okgoGet(this, UrlProtocol.PROBLEM_LIST, params, object : HttpUtilsInterface {
            override fun onSuccess(result: String?) {
                mLoadingDialog?.dismiss()
                Log.e("zzzz","getProblemList${result}")
                result?.let {
                  val bean=GsonUtil.GsonToBean(it,ProblemListVO::class.java)
                    bean.data?.let {data->
                        if (data.records.isNotEmpty()) {
                            dataList.clear()
                            messageAdapter?.notifyDataSetChanged()
                        }else{
                           changeViewNoData()
                        }
                    }
                }?:{
                    ToastUtils.toastShort(this@ProblemListActivity, getString(R.string.network_error))
                }.invoke()
            }

            override fun onError(code: Int, errorMsg: String?) {
                mLoadingDialog?.dismiss()
                ToastUtils.toastShort(this@ProblemListActivity, getString(R.string.network_error))
            }
        })
    }

    private fun  changeViewNoData(){
        rvProblemList.visibility=View.GONE
    }
}