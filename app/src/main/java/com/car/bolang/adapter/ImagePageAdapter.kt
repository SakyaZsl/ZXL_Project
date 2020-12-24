package com.car.bolang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.car.bolang.R
import com.car.bolang.activity.ImageActivity
import com.car.bolang.util.GlideUtils

class ImagePageAdapter(private var dataList:MutableList<String> ) :PagerAdapter(){

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(container.context).inflate(R.layout.item_image_list,null)
        val imageView=view.findViewById<ImageView>(R.id.ivProduct)
        imageView.setOnClickListener {
            ImageActivity.startAction(container.context,dataList[position])
        }

        GlideUtils.basisGlide(container.context,dataList[position],null,imageView)
        container.addView(view)
        return view
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int {
       return dataList.size
    }
}