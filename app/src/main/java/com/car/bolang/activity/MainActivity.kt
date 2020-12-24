package com.car.bolang.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentTransaction
import com.car.bolang.R
import com.car.bolang.common.BaseActivity
import com.car.bolang.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private var homeFragment: HomeFragmentNew? = null
    private var meFragment: MeFragment? = null
    private var msgFragment: NewsListFragment? = null
    private var problemFragment: ProblemListFragment? = null

    companion object {
        const val HOME_PAGE = 0
        const val MSG_PAGE = 1
        const val ME_PAGE = 2
        const val PROBLEM_PAGE = 3
        fun  startAction(context: Context){
            val intent=Intent(context,MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        }
    }


    override fun setLayoutResID(): Int {
        return R.layout.activity_main
    }


    override fun init() {
        showFragment(HOME_PAGE)
        llHomePage.setOnClickListener {
            showFragment(HOME_PAGE)
        }
        llMsgPage.setOnClickListener {
            showFragment(MSG_PAGE)
        }
        llMePage.setOnClickListener {
            showFragment(ME_PAGE)
        }
        llProblemPage.setOnClickListener {
            showFragment(PROBLEM_PAGE)
        }
    }

    override fun initData() {

    }

    private fun showFragment(position: Int) {
        val translation = supportFragmentManager.beginTransaction()
        hideFragment(translation)
        when (position) {
            HOME_PAGE -> {
                if (homeFragment == null) {
                    homeFragment = HomeFragmentNew()
                    translation.add(R.id.flContent, homeFragment!!)
                }
                translation.show(homeFragment!!)
                tvHomeMain.isSelected=true
                ivHomeMain.isSelected=true
            }
            MSG_PAGE -> {
                if (msgFragment == null) {
                    msgFragment = NewsListFragment()
                    translation.add(R.id.flContent, msgFragment!!)
                }
                translation.show(msgFragment!!)
                tvHomeMsg.isSelected=true
                ivHomeMsg.isSelected=true
                msgFragment?.getNewsList()
            }
            ME_PAGE -> {
                if (meFragment == null) {
                    meFragment = MeFragment()
                    translation.add(R.id.flContent, meFragment!!)
                }
                translation.show(meFragment!!)
                tvHomeMe.isSelected=true
                ivHomeMe.isSelected=true
            }
            PROBLEM_PAGE->{
                if (problemFragment == null) {
                    problemFragment = ProblemListFragment()
                    translation.add(R.id.flContent, problemFragment!!)
                }
                translation.show(problemFragment!!)
                tvHomeProblem.isSelected=true
                ivHomeProblem.isSelected=true
            }
            else -> {
            }
        }
        translation.commit()
    }

    private fun hideFragment(translation: FragmentTransaction) {
        homeFragment?.let {
            translation.hide(it)
        }

        msgFragment?.let {
            translation.hide(it)
        }
        meFragment?.let {
            translation.hide(it)
        }
        problemFragment?.let {
            translation.hide(it)
        }
        tvHomeMain.isSelected=false
        tvHomeMsg.isSelected=false
        tvHomeMe.isSelected=false
        tvHomeProblem.isSelected=false

        ivHomeMain.isSelected=false
        ivHomeMsg.isSelected=false
        ivHomeMe.isSelected=false
        ivHomeProblem.isSelected=false
    }


}
