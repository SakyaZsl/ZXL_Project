package com.car.bolang.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.car.bolang.R
import com.car.bolang.bean.GoldPrice


class GoldDetailAdapter(var list: List<GoldPrice>) :
    RecyclerView.Adapter<GoldDetailAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_gold_detail, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var bean=list[position]
        holder.tvGoldName.text=bean.name
        holder.tvGoldPrice.text=bean.price
        holder.tvGoldUp.text=bean.upAndDown
    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvGoldName=view.findViewById<TextView>(R.id.tvGoldName)
        var tvGoldUp=view.findViewById<TextView>(R.id.tvGoldUp)
        var tvGoldPrice=view.findViewById<TextView>(R.id.tvGoldPrice)
    }


}