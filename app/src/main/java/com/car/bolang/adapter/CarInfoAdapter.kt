package com.car.bolang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.bean.Goods
import com.car.bolang.bean.Record
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.util.Constants
import com.car.bolang.util.GlideUtils

class CarInfoAdapter (var context:Context,var list: List<Goods>) : RecyclerView.Adapter<CarInfoAdapter.MyViewHolder>() {

    private var onItemClickListenr :OnItemClickListener ?=null

    fun setonItemClickListenr(listener: OnItemClickListener){
        this.onItemClickListenr=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_car_info, parent, false)
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
        holder.tvCarName.text=list[position].name
        GlideUtils.basisGlide(context,Constants.HTTP+bean.img,null,holder.ivCarImg)

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvCarName=view.findViewById<TextView>(R.id.tvCarName)
        var ivCarImg=view.findViewById<ImageView>(R.id.ivCarImg)
    }


}