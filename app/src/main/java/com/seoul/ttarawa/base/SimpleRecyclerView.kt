package com.seoul.ttarawa.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class SimpleRecyclerView {

    abstract class Adapter<ITEM : Any, B : ViewDataBinding>(
        @LayoutRes private val layoutRes: Array<Int>,
        protected val bindingVariableId: Array<Int?>? = null
    ) : RecyclerView.Adapter<ViewHolder<B>>() {

        protected val items = mutableListOf<ITEM>()

        open fun replaceAll(items: List<ITEM>?) {
            items?.let {
                this.items.run {
                    clear()
                    addAll(it)
                }
            }
        }

        protected fun <T : ViewDataBinding> inflateBinding(parent: ViewGroup, viewType: Int): T =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layoutRes[viewType],
                parent,
                false
            )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<B> {
            return object : ViewHolder<B>(
                binding = inflateBinding(parent, viewType),
                bindingVariableId = bindingVariableId?.get(viewType)
            ) {}
        }

        override fun getItemCount(): Int = items.size

        override fun onBindViewHolder(holder: ViewHolder<B>, position: Int) {
            holder.onBindViewHolder(items[position])
        }
    }

    abstract class ViewHolder<out B : ViewDataBinding>(
        open val binding: B,
        private val bindingVariableId: Int?
    ) : RecyclerView.ViewHolder(binding.root) {

        open fun onBindViewHolder(item: Any?) {
            try {
                binding.run {
                    bindingVariableId?.let {
                        setVariable(it, item)
                    }
                    executePendingBindings()
                }
                itemView.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
                itemView.visibility = View.GONE
            }
        }
    }
}