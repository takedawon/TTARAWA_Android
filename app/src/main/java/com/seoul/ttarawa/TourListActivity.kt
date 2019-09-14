package com.seoul.ttarawa

import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.databinding.ActivityTourListBinding

class TourListActivity :
    BaseActivity<ActivityTourListBinding>(R.layout.activity_tour_list) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        initView()
    }

    override fun initView() {
        bind {
            val testModel : LocationTourModel = LocationTourModel(
                "http://tong.visitkorea.or.kr/cms/resource/04/2613904_image2_1.jpg",
                "가나다",
                "라마바사"
                ,"1.6km"
            )

            val testModel2 : LocationTourModel = LocationTourModel(
                "http://tong.visitkorea.or.kr/cms/resource/93/2600793_image2_1.jpg",
                "가나다",
                "라마바사"
                ,"1.6km"
            )
            val mAdapter = LocationBaseTourAdapter()
            mAdapter.addItem(testModel)
            mAdapter.addItem(testModel2)

            recyclerView.adapter = mAdapter

            val lm = LinearLayoutManager(this@TourListActivity)
            recyclerView.layoutManager = lm
            recyclerView.setHasFixedSize(true)

        }
    }
}
