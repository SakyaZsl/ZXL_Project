package com.car.bolang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.bean.GoodsVO
import com.car.bolang.inter.OnItemClickListener

class DeviceTypeAdapter  (var list: List<String>) : RecyclerView.Adapter<DeviceTypeAdapter.MyViewHolder>() {


    private var onItemClickListener : OnItemClickListener?=null

    fun setonItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_device_type, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var bean=list[position]
        holder.tvContent.text=bean
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClick(holder.itemView,position)
            }
        }

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvContent=view.findViewById<TextView>(R.id.tvContent)
    }


}