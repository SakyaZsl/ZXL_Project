package com.car.bolang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.bean.Record
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.GlideUtils

class ProductListAdapter  (var context: Context, var list: List<Record>) : RecyclerView.Adapter<ProductListAdapter.MyViewHolder>() {

    private var onItemClickListener : OnItemClickListener?=null

    fun setonItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_brand_info, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val bean=list[position]
        holder.tvCarName.text=bean.title
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClick(holder.itemView,position)
            }
        }
        val imgUrl=UrlProtocol.URL_IMG+bean.homeImg
        holder.tvCarPrice.text="价格：${bean.price}元"
        GlideUtils.basisGlide(context,imgUrl,null,holder.ivCarImg)

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvCarName=view.findViewById<TextView>(R.id.tvCarName)
        var ivCarImg=view.findViewById<ImageView>(R.id.ivCarImg)
        var tvCarPrice=view.findViewById<TextView>(R.id.tvCarPrice)
    }


}