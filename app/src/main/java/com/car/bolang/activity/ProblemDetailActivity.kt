package com.car.bolang.activity

import android.widget.Toast
import com.car.bolang.R
import com.car.bolang.common.BaseActivity
import com.car.bolang.util.ToastUtils
import kotlinx.android.synthetic.main.activity_common_head.*
import kotlinx.android.synthetic.main.activity_problem_detail.*

/**
 * 问题反馈activity
 */

class ProblemDetailActivity :BaseActivity() {
    override fun setLayoutResID(): Int {
        return R.layout.activity_problem_detail
    }

    override fun init() {
        ivBack.setOnClickListener { finish() }
        btnCommitProblem.setOnClickListener {
          ToastUtils.toastShort(this,"谢谢您的反馈")
            finish()
        }
    }

    override fun initData() {
    }
}