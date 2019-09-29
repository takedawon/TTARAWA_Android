package com.seoul.ttarawa.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.BaseSearchEntity
import com.seoul.ttarawa.data.entity.LocationTourModel
import com.seoul.ttarawa.data.entity.NaverFindModel
import com.seoul.ttarawa.ui.search.veiwholder.NaverFindViewHolder
import com.seoul.ttarawa.ui.search.veiwholder.TourViewHolder

class SearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<BaseSearchEntity>()

    var onClickStartDetail: ((model: BaseSearchEntity) -> Unit)? = null
    var onClickNaverSearch: ((model: BaseSearchEntity) -> Unit)? = null

    fun replaceAll(list: List<BaseSearchEntity>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    private var onClickSuggestRoute: ((routeKey: String) -> Unit)? = null

    fun setOnClickSuggestRoute(onClickSuggestRoute: ((routeKey: String) -> Unit)) {
        this.onClickSuggestRoute = onClickSuggestRoute
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == CategoryType.TOUR.code) {
            TourViewHolder(inflateDataBinding(parent, R.layout.item_tour), onClickStartDetail)
        } else if (viewType == CategoryType.CAFE.code) {
            NaverFindViewHolder(inflateDataBinding(parent, R.layout.item_naver_search), onClickNaverSearch)
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
            CategoryType.TOUR.code -> {
                (holder as? TourViewHolder)?.bind(list[position] as? LocationTourModel)
            }
            CategoryType.CAFE.code -> {
                (holder as? NaverFindViewHolder)?.bind(list[position] as? NaverFindModel)
            }
            else -> {
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return list[position].categoryCode
    }

    companion object {
        private const val TYPE_TOUR = 0
    }

}