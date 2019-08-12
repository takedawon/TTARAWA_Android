package com.seoul.ttarawa.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.seoul.ttarawa.R
import kotlinx.android.synthetic.main.fragment_home.view.*
import net.daum.mf.map.api.MapView

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val mapView = MapView(activity)
        val mapViewContainer = view.map_view

        mapView.isHDMapTileEnabled = true
        mapViewContainer.addView(mapView)


        return view
    }
}
