package com.seoul.ttarawa.ui.search.veiwholder

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.databinding.ItemTourBinding
import com.seoul.ttarawa.ui.search.TourDetailActivity

/**
 * 날씨가 나오는 뷰홀더
 */
class TourViewHolder(
    private val binding: ItemTourBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tour: LocationTourModel?,context:Context) {
        binding.run {
            tour?.let { tour ->
                Glide.with(itemView).load(tour.imgUrl).centerCrop().into(imgTourPhoto)
                txtTitle.text = tour.title
                txtDistance.text = ""
                txtAddress.text = tour.address
                layoutLocationTour.setOnClickListener {
                    val intent= Intent(context, TourDetailActivity::class.java)
                    intent.putExtra("contentId", tour.contentID)
                    intent.putExtra("title", tour.title)
                    intent.putExtra("mapX",tour.mapX)
                    intent.putExtra("mapY",tour.mapY)
                    context.startActivity(intent)
                }
            }
        }
    }
}