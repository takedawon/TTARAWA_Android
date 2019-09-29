package com.seoul.ttarawa.ui.search.veiwholder

import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.data.entity.NaverFindModel
import com.seoul.ttarawa.databinding.ItemNaverSearchBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.htmlToString

class NaverFindViewHolder(
    private val binding: ItemNaverSearchBinding,
    private val onClickStartDetail: ((model: NaverFindModel) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(naver: NaverFindModel?) {
        binding.run {
            naver?.let { naver ->
                txtNaverTitle.text = naver.title.htmlToString()
                txtNaverAddress.text = naver.address

                root click { onClickStartDetail?.invoke(naver) }
            }
        }
    }
}