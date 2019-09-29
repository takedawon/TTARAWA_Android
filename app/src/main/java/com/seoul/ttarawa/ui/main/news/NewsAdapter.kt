package com.seoul.ttarawa.ui.main.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.seoul.ttarawa.R

class NewsAdapter(val context: Context, val list:ArrayList<NewsData>) : RecyclerView.Adapter<NewsAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_news, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txtNewsTitle = itemView.findViewById<TextView>(R.id.txt_news_title)
        var txtNewsDate = itemView.findViewById<TextView>(R.id.txt_news_date)
        var imgNewsArrow = itemView.findViewById<ImageView>(R.id.img_news_arrow)
        var txtNewsContent = itemView.findViewById<TextView>(R.id.txt_news_content)
        var layout = itemView.findViewById<LinearLayout>(R.id.layout_news)
        var layoutContent = itemView.findViewById<LinearLayout>(R.id.layout_news_content)
        fun bind(news: NewsData) {
            txtNewsTitle.text = news.newstitle
            txtNewsDate.text = news.newsDate
            imgNewsArrow.setImageResource(R.drawable.down_arrow)
            txtNewsContent.text = news.newsContent

            if(news.state) {
                layoutContent.visibility = View.VISIBLE
                imgNewsArrow.setImageResource(R.drawable.up_arrow)
            } else {
                layoutContent.visibility = View.GONE
                imgNewsArrow.setImageResource(R.drawable.down_arrow)
            }

            layout.setOnClickListener {
                news.state = !news.state
                notifyItemChanged(adapterPosition)
            }
        }
    }
}