package com.car.bolang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.car.bolang.R
import com.car.bolang.inter.ShareAppCallback
import kotlinx.android.synthetic.main.fragment_share_app.*

class ShareAppFragment :DialogFragment() {

    var callback: ShareAppCallback?=null

    fun setShareCallback(callback: ShareAppCallback){
        this.callback=callback
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DialogFullScreen)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_share_app,container,false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }


    private fun initView(){
        ivShareApp.setOnClickListener {
            dismiss()
            callback?.let {
                it.onAppShare()
            }
        }
        parent.setOnClickListener { dismiss() }
    }
}