package com.seoul.ttarawa.ui.main.news

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.seoul.ttarawa.R
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityNewsBinding
import com.seoul.ttarawa.ui.main.news.NewsAdapter
import com.seoul.ttarawa.ui.main.news.NewsData

class NewsActivity : BaseActivity<ActivityNewsBinding>(
    R.layout.activity_news
) {
    private var newsList = arrayListOf<NewsData>()
    private lateinit var ref: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        ref = FirebaseDatabase.getInstance().getReference("NOTICE")
        ref.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for(data in p0.children) {
                    newsList.add(
                        NewsData(data.child("title").value.toString(),
                            data.child("date").value.toString(),
                            data.child("content").value.toString()))
                }
                newsList.reverse()
                val mAdapter = NewsAdapter(this@NewsActivity, newsList)
                bind {
                    recyclerNews.adapter = mAdapter

                    val lm = LinearLayoutManager(applicationContext)
                    recyclerNews.layoutManager = lm
                    recyclerNews.setHasFixedSize(true)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }

    override fun initView() {
        bind {
            btnNewsBack.setOnClickListener {
                finish()
            }
        }
    }
}
