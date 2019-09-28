package com.seoul.ttarawa.ui.search.veiwholder

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.seoul.ttarawa.data.entity.BaseSearchEntity
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.databinding.ItemTourBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ui.search.TourDetailActivity

/**
 * 날씨가 나오는 뷰홀더
 */
class TourViewHolder(
    private val binding: ItemTourBinding,
    private val onClickStartDetail: ((model: BaseSearchEntity) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(tour: LocationTourModel?) {
        binding.run {
            tour?.let { tour ->
                Glide.with(itemView).load(tour.imgUrl).centerCrop().into(imgTourPhoto)
                txtTitle.text = tour.title
                txtDistance.text = ""
                txtAddress.text = tour.address

                root click { onClickStartDetail?.invoke(tour) }
            }
        }
    }
}