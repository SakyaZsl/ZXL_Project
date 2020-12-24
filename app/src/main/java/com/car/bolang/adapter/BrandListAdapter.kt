package com.car.bolang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.bean.BrandRecord
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.network.UrlProtocol
import com.car.bolang.util.Constants
import com.car.bolang.util.GlideUtils

class BrandListAdapter (var context: Context, var list: List<BrandRecord>) : RecyclerView.Adapter<BrandListAdapter.MyViewHolder>() {

    private var onItemClickListenr : OnItemClickListener?=null

    fun setonItemClickListenr(listener: OnItemClickListener){
        this.onItemClickListenr=listener
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
        var bean=list[position]
        holder.itemView.setOnClickListener {
            onItemClickListenr?.let {
                it.onItemClick(holder.itemView,position)
            }
        }
        holder.tvCarName.text=bean.name
        holder.tvCarPrice.visibility=View.GONE
        GlideUtils.basisGlide(context, Constants.HTTP+bean.img,null,holder.ivCarImg)

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvCarName=view.findViewById<TextView>(R.id.tvCarName)
        var tvCarPrice=view.findViewById<TextView>(R.id.tvCarPrice)
        var ivCarImg=view.findViewById<ImageView>(R.id.ivCarImg)
    }


}