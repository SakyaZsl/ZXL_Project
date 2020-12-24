package com.car.bolang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.car.bolang.R
import com.car.bolang.inter.TakePictureCallback
import kotlinx.android.synthetic.main.fragment_select_picture.*

class SelectPictureFragment : DialogFragment(){

    private var callback: TakePictureCallback?=null

    fun setCallback(callback: TakePictureCallback){
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
        return inflater.inflate(R.layout.fragment_select_picture,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }


    private fun initView(){
        btnAlbum.setOnClickListener {
            dismiss()
            callback?.let {
                it.takeAlbum()
            }
        }
        btnCamera.setOnClickListener {
            dismiss()
            callback?.let {
                it.takeCamera()
            }

        }
        btnCancel.setOnClickListener { dismiss() }
    }
}