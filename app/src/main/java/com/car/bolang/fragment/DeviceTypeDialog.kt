package com.car.bolang.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.car.bolang.R
import com.car.bolang.adapter.DeviceTypeAdapter
import com.car.bolang.inter.ChooseCallBack
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.inter.TakePictureCallback
import kotlinx.android.synthetic.main.dialog_device_type.*
import kotlinx.android.synthetic.main.fragment_select_picture.*

class DeviceTypeDialog : DialogFragment(), OnItemClickListener {


    private var callback: ChooseCallBack? = null
    private var dataList: MutableList<String> = ArrayList()
    private var adapter: DeviceTypeAdapter? = null

    fun setData(dataList: List<String>) {
        this.dataList.clear()
        this.dataList.addAll(dataList)
        adapter?.notifyDataSetChanged()
    }

    fun setCallback(callback: ChooseCallBack) {
        this.callback = callback
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
        return inflater.inflate(R.layout.dialog_device_type, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }


    private fun initView() {
        adapter = DeviceTypeAdapter(dataList)
        adapter?.setonItemClickListener(this)
        rvContent.adapter = adapter
        rvContent.layoutManager = LinearLayoutManager(activity)
    }


    override fun onItemClick(view: View?, position: Int) {
        callback?.let {
            dismiss()
            it.onChoose(dataList[position],position)
        }
    }
}