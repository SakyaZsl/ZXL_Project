package com.car.bolang.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.car.bolang.R
import com.car.bolang.bean.NewData
import com.car.bolang.inter.OnItemClickListener
import com.car.bolang.util.GlideUtils

class NewsListAdapter (var context: Context, var list: List<NewData>) : RecyclerView.Adapter<NewsListAdapter.MyViewHolder>() {

    private var onItemClickListener : OnItemClickListener?=null

    fun setonItemClickListener(listener: OnItemClickListener){
        this.onItemClickListener=listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news_detail, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var bean=list[position]
        if(TextUtils.isEmpty(bean.firstPicture)){
            holder.ivNewsTitle.visibility=View.GONE
        }else{
            holder.ivNewsTitle.visibility=View.VISIBLE
            GlideUtils.basisGlide(context,bean.firstPicture,null,holder.ivNewsTitle)
        }
        holder.tvNewsContent.text=bean.createTime
        holder.tvNewsTitle.text=bean.title
        holder.itemView.setOnClickListener {
            onItemClickListener?.let {
                it.onItemClick(holder.itemView,position)
            }
        }
    }

    class MyViewHolder (view: View): RecyclerView.ViewHolder(view){
        var ivNewsTitle=view.findViewById<ImageView>(R.id.ivNewsTitle)
        var tvNewsContent=view.findViewById<TextView>(R.id.tvNewsContent)
        var tvNewsTitle=view.findViewById<TextView>(R.id.tvNewsTitle)
    }


}