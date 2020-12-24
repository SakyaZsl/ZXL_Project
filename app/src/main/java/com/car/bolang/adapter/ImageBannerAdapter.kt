package com.car.bolang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.util.GlideUtils
import com.youth.banner.adapter.BannerAdapter

class ImageBannerAdapter ( datas:List<String>) :BannerAdapter<String,ImageBannerAdapter.MyViewHolder> (datas){

    private var  mContext:Context?=null

    fun setContext(context: Context){
        mContext=context
    }
    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
        val view=LayoutInflater.from(mContext).inflate(R.layout.item_image_banner,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindView(holder: MyViewHolder, data: String?, position: Int, size: Int) {
        GlideUtils.basisGlide(mContext,mDatas[position],null,holder.ivProduct)
    }


    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var ivProduct=view.findViewById<ImageView>(R.id.ivProduct)
    }

}