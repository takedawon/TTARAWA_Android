package com.seoul.ttarawa.ui.search.veiwholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.databinding.ItemTourBinding

/**
 * 날씨가 나오는 뷰홀더
 */
class TourViewHolder(
    private val binding: ItemTourBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tour: LocationTourModel?) {
        binding.run {
            tour?.let { tour ->
                Glide.with(itemView).load(tour.imgUrl).centerCrop().into(imgTourPhoto)
                txtTitle.text = tour.title
                txtDistance.text = tour.distance
                txtAddress.text = tour.address
            }
        }
    }
}