package com.seoul.ttarawa.ui.mypath

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.Path
import com.seoul.ttarawa.databinding.ItemMyPathBinding
import com.seoul.ttarawa.ext.click

class MyPathAdapter : RecyclerView.Adapter<MyPathAdapter.MyPathViewHolder>() {

    private val list = mutableListOf<Path>()

    var onClickStartPathActivity: ((pathId: String, pathDate: String) -> Unit)? = null

    fun replaceAll(list: List<Path>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPathViewHolder {
        return MyPathViewHolder(
            inflateDataBinding(parent, R.layout.item_my_path),
            onClickStartPathActivity
        )
    }

    private fun <B : ViewDataBinding> inflateDataBinding(parent: ViewGroup, layoutId: Int) =
        DataBindingUtil.inflate<B>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MyPathViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class MyPathViewHolder(
        private val binding: ItemMyPathBinding,
        private val onClickStartPathActivity: ((pathId: String, pathDate: String) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(path: Path) {
            binding.run {
                tvMyPathTitle.text = path.title
                btnMyPathSharing.isSelected = path.shareYn

                cvSuggest click { onClickStartPathActivity?.invoke(path.id, path.date) }
            }
        }
    }
}
