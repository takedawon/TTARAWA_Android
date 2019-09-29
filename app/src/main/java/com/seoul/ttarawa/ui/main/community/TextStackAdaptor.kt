package com.seoul.ttarawa.ui.main.community

import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.loopeer.cardstack.CardStackView
import com.loopeer.cardstack.StackAdapter
import com.seoul.ttarawa.R

class TestStackAdapter(context: Context) : StackAdapter<Int>(context) {

    override fun bindView(data: Int?, position: Int, holder: CardStackView.ViewHolder) {
        if (holder is ColorItemLargeHeaderViewHolder) {
            holder.onBind(data, position)
        }
        if (holder is ColorItemWithNoHeaderViewHolder) {
            holder.onBind(data, position)
        }
        if (holder is ColorItemViewHolder) {
            holder.onBind(data, position)
        }
    }

    override fun onCreateView(parent: ViewGroup, viewType: Int): CardStackView.ViewHolder {
        val view: View
        when (viewType) {
            R.layout.list_card_item_larger_header -> {
                view = layoutInflater.inflate(
                    R.layout.list_card_item_larger_header,
                    parent,
                    false
                )
                return ColorItemLargeHeaderViewHolder(view)
            }
            R.layout.list_card_item_with_no_header -> {
                view =
                    layoutInflater.inflate(
                        R.layout.list_card_item_with_no_header,
                        parent,
                        false
                    )
                return ColorItemWithNoHeaderViewHolder(view)
            }
            else -> {
                view = layoutInflater.inflate(R.layout.list_card_item, parent, false)
                return ColorItemViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 6) {//TODO TEST LARGER ITEM
            R.layout.list_card_item_larger_header
        } else if (position == 10) {
            R.layout.list_card_item_with_no_header
        } else {
            R.layout.list_card_item
        }
    }

    internal class ColorItemViewHolder(view: View) : CardStackView.ViewHolder(view) {
        private var mLayout: View = view.findViewById(R.id.frame_list_card_item)
        private var mContainerContent: View = view.findViewById(R.id.container_list_content)
        private var mTextTitle: TextView = view.findViewById(R.id.text_list_card_title)

        override fun onItemExpand(b: Boolean) {
            mContainerContent.visibility = if (b) View.VISIBLE else View.GONE
        }

        fun onBind(data: Int?, position: Int) {
            mLayout.background
                .setColorFilter(ContextCompat.getColor(context, data!!), PorterDuff.Mode.SRC_IN)
            mTextTitle.text = position.toString()
        }

    }

    internal class ColorItemWithNoHeaderViewHolder(view: View) : CardStackView.ViewHolder(view) {
        private var mLayout: View = view.findViewById(R.id.frame_list_card_item)
        private var mTextTitle : TextView = view.findViewById(R.id.text_list_card_title)

        override fun onItemExpand(b: Boolean) {}

        fun onBind(data: Int?, position: Int) {
            mLayout.getBackground()
                .setColorFilter(ContextCompat.getColor(context, data!!), PorterDuff.Mode.SRC_IN)
            mTextTitle.text = position.toString()
        }

    }

    internal class ColorItemLargeHeaderViewHolder(view: View) : CardStackView.ViewHolder(view) {
        private var mLayout: View = view.findViewById(R.id.frame_list_card_item)
        private var mContainerContent: View = view.findViewById(R.id.container_list_content)
        private var mTextTitle: TextView = view.findViewById(R.id.text_list_card_title)

        override fun onItemExpand(b: Boolean) {
            mContainerContent.visibility = if (b) View.VISIBLE else View.GONE
        }

        override fun onAnimationStateChange(state: Int, willBeSelect: Boolean) {
            super.onAnimationStateChange(state, willBeSelect)
            if (state == CardStackView.ANIMATION_STATE_START && willBeSelect) {
                onItemExpand(true)
            }
            if (state == CardStackView.ANIMATION_STATE_END && !willBeSelect) {
                onItemExpand(false)
            }
        }

        fun onBind(data: Int?, position: Int) {
            mLayout.background
                .setColorFilter(ContextCompat.getColor(context, data!!), PorterDuff.Mode.SRC_IN)
            mTextTitle.text = position.toString()

            itemView.findViewById<View>(R.id.text_view)
                .setOnClickListener {
                    (itemView.parent as CardStackView).performItemClick(this@ColorItemLargeHeaderViewHolder)
                }
        }
    }
}