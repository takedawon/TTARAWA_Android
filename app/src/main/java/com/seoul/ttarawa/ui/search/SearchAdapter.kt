package com.seoul.ttarawa.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.ui.search.veiwholder.TourViewHolder

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<Any>()

    fun replaceAll(list: List<Any>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private var onClickSuggestRoute: ((routeKey: String) -> Unit)? = null

    fun setOnClickSuggestRoute(onClickSuggestRoute: ((routeKey: String) -> Unit)) {
        this.onClickSuggestRoute = onClickSuggestRoute
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_TOUR) {
            TourViewHolder(inflateDataBinding(parent, R.layout.item_tour))
        } else {
            super.createViewHolder(parent, viewType)
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
        when (getItemViewType(position)) {
            TYPE_TOUR -> {
                (holder as? TourViewHolder)?.bind(list[position] as? LocationTourModel)
            }
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_TOUR
        } else {
            super.getItemViewType(position)
        }
    }

    companion object {
        private const val TYPE_TOUR = 0
    }

}