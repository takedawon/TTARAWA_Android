package com.seoul.ttarawa.ui.path

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.PathEntity
import com.seoul.ttarawa.databinding.ItemPathAddBinding
import com.seoul.ttarawa.databinding.ItemPathBinding

class PathAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = mutableListOf<PathEntity>()

    init {
        replaceAll(listOf(
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false),
            PathEntity("abcd", "", "", "10:20", "20:30", false)
        ))
    }

    fun changeDeleteMode(isChecked: Boolean) {
        list.forEach { it.isVisibleDelete = isChecked }
        notifyDataSetChanged()
    }

    fun add(path: PathEntity) {
        this.list.add(path)
        notifyDataSetChanged()
    }

    fun replaceAll(list: List<PathEntity>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_PATH_ADD) {
            PathAddViewHolder(inflateDataBinding(parent, R.layout.item_path_add))
        } else {
            PathViewHolder(inflateDataBinding(parent, R.layout.item_path))
        }
    }

    private fun <B : ViewDataBinding> inflateDataBinding(parent: ViewGroup, layoutId: Int) =
        DataBindingUtil.inflate<B>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

    override fun getItemCount(): Int = list.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_PATH) {
            (holder as PathViewHolder).bind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == list.size) {
            return TYPE_PATH_ADD
        }
        return super.getItemViewType(position)
    }

    class PathViewHolder(val binding: ItemPathBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(path: PathEntity) {
            binding.tvPathName.text = path.name
            binding.tvPathTime.text = "${path.startTime} ~ ${path.endTime}"
            binding.ivPathDelete.visibility = if (path.isVisibleDelete) View.VISIBLE else View.GONE
        }
    }

    class PathAddViewHolder(val binding: ItemPathAddBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    companion object {
        const val TYPE_PATH = 0
        const val TYPE_PATH_ADD = 1
    }
}