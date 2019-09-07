package com.seoul.ttarawa.ui.main.home

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.SuggestRouteModel
import com.seoul.ttarawa.data.entity.WeatherModel
import com.seoul.ttarawa.databinding.ItemHomeSuggestRouteBinding
import com.seoul.ttarawa.databinding.ItemHomeWeatherBinding
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.module.GlideApp

class HomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val list = mutableListOf<Any>()

    fun replaceAll(list: List<Any>) {
        this.list.clear()
        notifyItemRangeRemoved(0, list.size)
        this.list.addAll(list)
        notifyItemRangeInserted(0, list.size)
    }

    private var onClickSuggestRoute: ((routeKey: String) -> Unit)? = null

    fun setOnClickSuggestRoute(onClickSuggestRoute: ((routeKey: String) -> Unit)) {
        this.onClickSuggestRoute = onClickSuggestRoute
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == TYPE_WEATHER) {
            WeatherViewHolder(inflateDataBinding(parent, R.layout.item_home_weather))
        } else {
            SuggestRouteViewHolder(
                inflateDataBinding(parent, R.layout.item_home_suggest_route),
                onClickSuggestRoute
            )
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
        if (getItemViewType(position) == TYPE_WEATHER) {
            (holder as? WeatherViewHolder)?.bind(list[position] as? WeatherModel)
        } else {
            (holder as? SuggestRouteViewHolder)?.bind(list[position] as? SuggestRouteModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            TYPE_WEATHER
        } else {
            TYPE_SUGGEST_ROUTE
        }
    }

    /**
     * 날씨가 나오는 뷰홀더
     */
    class WeatherViewHolder(
        private val binding: ItemHomeWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherModel: WeatherModel?) {
            weatherModel?.let {
                binding.tvWeatherUserName.text = it.userName
                binding.tvWeatherState.text = it.weatherState
            }
        }
    }


    /**
     * 추천 경로 뷰홀더
     */
    class SuggestRouteViewHolder(
        private val binding: ItemHomeSuggestRouteBinding,
        private val onClickSuggestRoute: ((routeKey: String) -> Unit)?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(suggestRouteModel: SuggestRouteModel?) {
            suggestRouteModel?.let {
                binding.run {
                    tvSuggestTitle.text = it.title
                    tvSuggestTitle.setTextColor(ContextCompat.getColor(binding.root.context, suggestRouteModel.textColor))

                    tvSuggestSubTitle.text = it.subTitle
                    tvSuggestSubTitle.setTextColor(ContextCompat.getColor(binding.root.context, suggestRouteModel.textColor))

                    initCardViewBackground(it.imgUri)

                    cvSuggest click { onClickSuggestRoute?.invoke(suggestRouteModel.routeKey) }

                }
            }
        }

        private fun initCardViewBackground(imgUri: String) {
            GlideApp.with(binding.root)
                .load(imgUri)
                .error(R.drawable.ic_launcher_background)
                .transform(RoundedCorners(8))
                .into<Target<Drawable>>(
                    object : CustomViewTarget<CardView, Drawable>(binding.cvSuggest) {
                        override fun onLoadFailed(errorDrawable: Drawable?) {
                            binding.ivSuggest.background = errorDrawable
                        }

                        override fun onResourceCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                            binding.ivSuggest.background = resource
                        }

                    }

                )
        }
    }

    companion object {
        private const val TYPE_WEATHER = 0
        private const val TYPE_SUGGEST_ROUTE = 1
    }

}