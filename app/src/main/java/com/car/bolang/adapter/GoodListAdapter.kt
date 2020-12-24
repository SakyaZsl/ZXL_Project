package com.car.bolang.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.bean.Record
import com.car.bolang.inter.OnItemClickListener

class GoodListAdapter (var list: List<Record>) : RecyclerView.Adapter<GoodListAdapter.MyViewHolder>() {

    private var onItemClickListener : OnItemClickListener?=null

    fun setonItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_content, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var bean=list[position]
        holder.tvGoodName.text=bean.name
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClick(holder.itemView,position)
            }
        }

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvGoodName=view.findViewById<TextView>(R.id.tvGoodName)
    }


}