package com.seoul.ttarawa.ui.map

import android.graphics.Color
import android.os.Bundle
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import com.seoul.ttarawa.databinding.ActivityPathBinding
import com.seoul.ttarawa.module.NetworkModule
import net.daum.mf.map.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * 지도 + 경로
 */
class PathActivity : BaseActivity<ActivityPathBinding>(
    com.seoul.ttarawa.R.layout.activity_path
) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()

    }

    override fun initView() {
        bind {
            rlPathMap.addView(MapView(this@PathActivity).apply {
                isHDMapTileEnabled = true
            })
            getRoadPath("37.47276907","126.89075388","37.47371341","126.89094828")

        }
    }

    private fun getRoadPath(startLat:String, startLon:String, endLat:String, endLon:String) {
        NetworkModule.tmapWalkingApi.getWalkingPath(
            appKey = "d21dbb20-70b0-4824-8b51-cf3cc6fd8aca",
            version = "1",
            startName = "123",
            endName = "456",
            startX = startLon,
            startY = startLat,
            endX = endLon,
            endY = endLat
        ).enqueue(object:Callback<TmapWalkingResponse?> {
            override fun onFailure(call: Call<TmapWalkingResponse?>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<TmapWalkingResponse?>,
                response: Response<TmapWalkingResponse?>
            ) {
                val mapView = MapView(this@PathActivity)
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(
                    startLon.toDouble(),startLat.toDouble()),true)

                val polyline=MapPolyline()
                val repo = response.body()
                val naviPoints= arrayListOf<PointDouble>()
                repo?.let {
                    val featuresSize = it.features.size


                    polyline.lineColor = Color.RED
                    polyline.addPoint(
                        MapPoint.mapPointWithGeoCoord(
                            startLat.toDouble(), startLon.toDouble()))

                    for (i in 0 until featuresSize) {
                        val type = repo.features[i].geometry.type
                        val points: PointDouble
                        if (type == "Point") {
                            points = PointDouble(
                                repo.features[i].geometry.coordinates[1],
                                repo.features[i].geometry.coordinates[0]
                            )
                            val marketPoint3 =
                                MapPoint.mapPointWithGeoCoord(points.lat, points.lon)
                            naviPoints.add(points)
                            val marker3 = MapPOIItem() // 마커 생성
                            marker3.itemName =
                                repo.features[i].properties.description
                            marker3.tag = 3
                            marker3.mapPoint = marketPoint3
                            marker3.markerType = MapPOIItem.MarkerType.RedPin

                            mapView.addPOIItem(marker3)

                            polyline.addPoint(
                                MapPoint.mapPointWithGeoCoord(
                                    points.lat,
                                    points.lon
                                )
                            )
                        } else if (type == "LineString") {
                            val list = repo.features[i].geometry.coordinates
                            for (k in list.indices) { // k
                                val lit = list[k].toString().split(" ".toRegex())
                                    .dropLastWhile({ it.isEmpty() }).toTypedArray()
                                val lineX =
                                    java.lang.Double.parseDouble(lit[0].substring(1, lit[0].length - 1))
                                val lineY =
                                    java.lang.Double.parseDouble(lit[1].substring(0, lit[1].length - 1))
                                polyline.addPoint(MapPoint.mapPointWithGeoCoord(lineY, lineX))
                            }
                        }
                    }
                    val padding = 150 // px
                    val mapPointBounds = MapPointBounds(polyline.mapPoints)
                    mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds, padding))
                }
        }
        })
    }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
        const val EXTRA_SUGGEST_ROUTE_KEY = "EXTRA_SUGGEST_ROUTE_KEY"
    }
}

class PointDouble(lat: Double, lon: Double) {
    var lat:Double = 0.0
    var lon:Double = 0.0
}
