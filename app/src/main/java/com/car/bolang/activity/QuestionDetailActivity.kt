package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.car.bolang.R
import com.car.bolang.bean.ProblemRecord
import com.car.bolang.common.BaseActivity
import com.car.bolang.util.Constants
import com.car.bolang.util.GlideUtils
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_question_detail.*

class QuestionDetailActivity :BaseActivity() {

    private var mBean:ProblemRecord?=null
    companion object {
        fun  startAction(context: Context,bean: ProblemRecord){
            val intent= Intent(context,QuestionDetailActivity::class.java)
            intent.putExtra(Constants.GOOD_NAME,bean)
            context.startActivity(intent)
        }
    }
    override fun setLayoutResID(): Int {
        return R.layout.activity_question_detail
    }

    override fun init() {
        tvTitle.text="问题详情"
        mBean= intent.getSerializableExtra(Constants.GOOD_NAME) as ProblemRecord?
        tvProblemName.text=mBean?.describes
        tvProblemTime.text=mBean?.createTime
        if(TextUtils.isEmpty(mBean?.answer)){
            tvOurAnswer.visibility= View.GONE
        }else{
            tvOurAnswer.visibility= View.VISIBLE
            tvOurAnswer.text= mBean?.answer
        }
        if(TextUtils.isEmpty(mBean?.img)){
            ivShowProblem.visibility= View.GONE
        }else{
            ivShowProblem.visibility= View.VISIBLE
            GlideUtils.basisGlide(this,mBean?.img,null,ivShowProblem)
        }
        ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    override fun initData() {

    }
}