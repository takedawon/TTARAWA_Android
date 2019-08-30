package com.seoul.ttarawa.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.seoul.ttarawa.R
import kotlinx.android.synthetic.main.activity_plan.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import net.daum.mf.map.api.MapView

class PlanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan)

        val mapView = MapView(this)
        val mapViewContainer = map_plan_view

        mapView.isHDMapTileEnabled = true
        mapViewContainer.addView(mapView)
    }
}
