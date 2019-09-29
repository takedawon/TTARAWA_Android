package com.seoul.ttarawa.ui.path

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.WayPointEntity
import com.seoul.ttarawa.databinding.ItemPathAddBinding
import com.seoul.ttarawa.databinding.ItemPathBinding

class PathAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val list = mutableListOf<WayPointEntity>()

    fun changeDeleteMode(isChecked: Boolean) {
        list.forEach { it.isVisibleDelete = isChecked }
        notifyDataSetChanged()
    }

    fun add(wayPoint: WayPointEntity) {
        this.list.add(wayPoint)
        notifyDataSetChanged()
    }

    fun replaceAll(list: List<WayPointEntity>) {
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

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_PATH) {
            (holder as PathViewHolder).bind(list[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun getItem(position: Int): WayPointEntity? {
        return if (position >= list.size) {
            null
        } else {
            list[position]
        }
    }

    class PathViewHolder(val binding: ItemPathBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(wayPoint: WayPointEntity) {
            binding.tvPathName.text = wayPoint.name
            binding.tvPathTime.text = wayPoint.address
            binding.ivPathDelete.visibility = if (wayPoint.isVisibleDelete) View.VISIBLE else View.GONE
        }
    }

    class PathAddViewHolder(val binding: ItemPathAddBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    companion object {
        const val TYPE_PATH = 0
        const val TYPE_PATH_ADD = 1
    }
}