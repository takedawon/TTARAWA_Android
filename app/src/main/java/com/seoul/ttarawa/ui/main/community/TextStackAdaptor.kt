package com.seoul.ttarawa.ui.main.community

import android.content.Context
import android.graphics.PorterDuff
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.loopeer.cardstack.CardStackView
import com.loopeer.cardstack.StackAdapter
import com.seoul.ttarawa.R
import com.seoul.ttarawa.data.entity.CommunityEntity
import com.seoul.ttarawa.ext.click
import com.seoul.ttarawa.ext.simpleDateFormat

class TestStackAdapter(context: Context) : StackAdapter<CommunityEntity>(context) {

    override fun bindView(data: CommunityEntity?, position: Int, holder: CardStackView.ViewHolder) {
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

    var onClickStartPathActivity: ((pathId: String, pathDate: String) -> Unit)? = null

    override fun onCreateView(parent: ViewGroup, viewType: Int): CardStackView.ViewHolder {
        val view: View = layoutInflater.inflate(R.layout.list_card_item, parent, false)
        return ColorItemViewHolder(view, onClickStartPathActivity)
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.list_card_item
        // if (position == 6) {//
        //     R.layout.list_card_item_larger_header
        // } else if (position == 10) {
        //     R.layout.list_card_item_with_no_header
        // } else {
        //     R.layout.list_card_item
        // }
    }

    /**
     * 사용
     */
    internal class ColorItemViewHolder(
        view: View,
        private val onClickStartPathActivity: ((pathId: String, pathDate: String) -> Unit)?
    ) : CardStackView.ViewHolder(view) {
        private var mLayout: View = view.findViewById(R.id.frame_list_card_item)
        private var mContainerContent: View = view.findViewById(R.id.container_list_content)
        private var mTextTitle: TextView = view.findViewById(R.id.text_list_card_title)
        private var mTextUserName: TextView = view.findViewById(R.id.text_list_card_user_name)
        private var mTextDate: TextView = view.findViewById(R.id.text_list_card_date)
        private var mIvBg: ImageView = view.findViewById(R.id.iv_list_bg)

        override fun onItemExpand(b: Boolean) {
            // mContainerContent.visibility = if (b) View.VISIBLE else View.GONE
        }

        fun onBind(data: CommunityEntity?, position: Int) {
            data?.let {
                mIvBg.background = ContextCompat.getDrawable(mIvBg.context, it.backgroundResId)
                mTextTitle.text = it.title
                mTextUserName.text = it.userName
                mTextDate.text = it.date.simpleDateFormat(original = "yyyyMMdd", format = "yyyy-MM-dd")

                itemView click { _ ->
                    onClickStartPathActivity?.invoke(it.id, it.date)
                }
            }
        }

    }

    internal class ColorItemWithNoHeaderViewHolder(view: View) : CardStackView.ViewHolder(view) {
        private var mLayout: View = view.findViewById(R.id.frame_list_card_item)
        private var mTextTitle: TextView = view.findViewById(R.id.text_list_card_title)

        override fun onItemExpand(b: Boolean) {}

        fun onBind(data: CommunityEntity?, position: Int) {
            data?.let {
                mTextTitle.text = it.title
            }
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

        fun onBind(data: CommunityEntity?, position: Int) {
            data?.let {
                mTextTitle.text = it.title

                itemView.findViewById<View>(R.id.text_view)
                    .setOnClickListener {
                        (itemView.parent as CardStackView).performItemClick(this@ColorItemLargeHeaderViewHolder)
                    }
            }
        }
    }
}