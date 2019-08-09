package com.seoul.ttarawa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_home.*
import net.daum.android.map.MapView

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mapView = MapView(activity)
        val mapViewContainer = map_view
        mapViewContainer.addView(mapView)

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}
