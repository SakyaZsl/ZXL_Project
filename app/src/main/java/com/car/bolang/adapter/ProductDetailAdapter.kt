package com.car.bolang.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.util.GlideUtils

class ProductDetailAdapter  (var context: Context, var list: List<String>) : RecyclerView.Adapter<ProductDetailAdapter.MyViewHolder>() {

    private var onItemClickListener : OnItemClickListener?=null

    fun setonItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        GlideUtils.basisGlide(context,list[position],null,holder.ivProduct)
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClick(holder.itemView,position)
            }
        }
    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var ivProduct=view.findViewById<ImageView>(R.id.ivProduct)
    }


}