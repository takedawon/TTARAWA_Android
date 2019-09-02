package com.seoul.ttarawa.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.seoul.ttarawa.R
import kotlinx.android.synthetic.main.activity_plan.*
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
