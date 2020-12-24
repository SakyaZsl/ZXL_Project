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

class GoodsGridAdapter (var list: List<GoodsVO>) : RecyclerView.Adapter<GoodsGridAdapter.MyViewHolder>() {

    private var imageList= listOf(R.drawable.icon_device_01,R.drawable.icon_device_02,R.drawable.icon_device_06,
        R.drawable.icon_device_04,R.drawable.icon_device_05,R.drawable.icon_device_03)

    private var onItemClickListener : OnItemClickListener?=null

    fun setonItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_goods, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var bean=list[position]
        holder.tvGoodName.text=bean.name
        holder.ivGoodName.setImageResource(imageList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClick(holder.itemView,position)
            }
        }

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvGoodName=view.findViewById<TextView>(R.id.tvGoodName)
        var ivGoodName=view.findViewById<ImageView>(R.id.ivGoodName)
    }


}