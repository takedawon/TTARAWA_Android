package com.seoul.ttarawa.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.databinding.ItemTourImageBinding


class TourImageAdapter :
    RecyclerView.Adapter<TourImageAdapter.TourImageViewHolder>() {
    var list: ArrayList<String> = ArrayList()

    fun replaceAll(list: List<String>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tour_image, parent, false)
        return TourImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TourImageViewHolder, position: Int) {
        holder.binding.imageUrl = list[position]
    }

    class TourImageViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding: ItemTourImageBinding = DataBindingUtil.bind(itemView)!!
    }
}