package com.seoul.ttarawa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seoul.ttarawa.data.entity.LocationTourModel

class LocationBaseTourAdapter : RecyclerView.Adapter<LocationBaseTourAdapter.ViewHolder>() {
    private val items: ArrayList<LocationTourModel> = ArrayList()

    fun addItem(item: LocationTourModel) {
        items.add(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).
            inflate(R.layout.custom_tour_list, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: LocationTourModel = items[position]
        holder.setItem(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageTourPhoto = itemView.findViewById<ImageView>(R.id.img_tour_photo)
        private val txtTitle = itemView.findViewById<TextView>(R.id.txt_title)
        private val txtAddress = itemView.findViewById<TextView>(R.id.txt_address)
        private val txtDistance = itemView.findViewById<TextView>(R.id.txt_distance)

        fun setItem(model: LocationTourModel) {
            Glide.with(itemView).load(model.imgUrl).into(imageTourPhoto)
            txtTitle.text = model.title
            txtDistance.text = model.distance
            txtAddress.text = model.address
        }

    }
}