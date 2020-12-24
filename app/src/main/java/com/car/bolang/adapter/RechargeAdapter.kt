package com.car.bolang.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.bean.MemberRecord
import com.car.bolang.inter.OnItemClickListener

class RechargeAdapter (var context: Context, var list: List<MemberRecord>) : RecyclerView.Adapter<RechargeAdapter.MyViewHolder>() {
    private var currentPosition=0
    private var onItemClickListenr : OnItemClickListener?=null

    fun setonItemClickListenr(listener: OnItemClickListener){
        this.onItemClickListenr=listener
    }

    fun setCurrentPosition(position: Int){
        currentPosition=position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recharge, parent, false)
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
        holder.tvCardName.text=bean.name
        holder.tvCardPrice.text=bean.amount.toString()

        if(currentPosition==position){
            holder.tvCardName.isSelected=true
            holder.tvCardPrice.isSelected=true
            holder.tvCardRmb.isSelected=true
        }else{
            holder.tvCardName.isSelected=false
            holder.tvCardPrice.isSelected=false
            holder.tvCardRmb.isSelected=false
        }

    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var tvCardName=view.findViewById<TextView>(R.id.tvCardName)
        var tvCardPrice=view.findViewById<TextView>(R.id.tvCardPrice)
        var tvCardRmb=view.findViewById<TextView>(R.id.tvCardRmb)
    }


}