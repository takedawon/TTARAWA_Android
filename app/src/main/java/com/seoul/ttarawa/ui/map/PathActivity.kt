package com.seoul.ttarawa.ui.map

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.seoul.ttarawa.base.BaseActivity
import com.seoul.ttarawa.data.remote.response.TmapWalkingResponse
import com.seoul.ttarawa.databinding.ActivityPathBinding
import com.seoul.ttarawa.module.NetworkModule
import net.daum.mf.map.api.*
import org.jetbrains.anko.toast
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
            val mapView = MapView(this@PathActivity)
            flPathMap.addView(mapView.apply {
                isHDMapTileEnabled = true
            })
            getRoadPath("37.47276907", "126.89075388", "37.47371341", "126.89094828", mapView)

        }
    }

    private fun getRoadPath(
        startLat: String,
        startLon: String,
        endLat: String,
        endLon: String,
        mapView: MapView
    ) {
        NetworkModule.tmapWalkingApi.getWalkingPath(
            appKey = "d21dbb20-70b0-4824-8b51-cf3cc6fd8aca",
            version = "1",
            startName = "123",
            endName = "456",
            startX = startLon,
            startY = startLat,
            endX = endLon,
            endY = endLat
        ).enqueue(object : Callback<TmapWalkingResponse?> {
            override fun onFailure(call: Call<TmapWalkingResponse?>, t: Throwable) {
                t.printStackTrace()
                toast("실패")
                Log.e("경로에러", call.toString())
                Log.e("경로에러", t.localizedMessage)

            }

            override fun onResponse(
                call: Call<TmapWalkingResponse?>,
                response: Response<TmapWalkingResponse?>
            ) {
                toast("성공")
                //Lon,Lat 순으로 입력할 것.
                mapView.setMapCenterPoint(
                    MapPoint.mapPointWithGeoCoord(
                        startLat.toDouble(), startLon.toDouble()
                    ), true
                ) // 중심점 이동

                val polyline = MapPolyline()
                val repo = response.body()
                val naviPoints = arrayListOf<PointDouble>()

                Log.e("startLat", startLat)
                Log.e("startLon", startLon)
                val featuresSize = response.body()?.features?.size ?: 0
                polyline.lineColor = Color.RED // 폴리라인 컬러 지정
                polyline.addPoint(
                    MapPoint.mapPointWithGeoCoord(
                        startLat.toDouble(), startLon.toDouble()
                    )
                ) // 시작점 추가

                repo?.let {
                    for (i in 0 until featuresSize) {
                        val type = repo.features[i].geometry.type
                        Log.e("테스트: type", type)

                        if (type == "Point") {
                            val points = PointDouble(
                                repo.features[i].geometry.coordinates[1] as Double,
                                repo.features[i].geometry.coordinates[0] as Double
                            )

                            val marketPoint3 =
                                MapPoint.mapPointWithGeoCoord(points.lat, points.lon)
                            naviPoints.add(points)
                            val marker5 = MapPOIItem() // 마커 생성
                            marker5.itemName = repo.features[i].properties.description
                            marker5.tag = 4
                            marker5.mapPoint = marketPoint3
                            marker5.markerType = MapPOIItem.MarkerType.RedPin

                            mapView.addPOIItem(marker5)

                            polyline.addPoint(MapPoint.mapPointWithGeoCoord(points.lat, points.lon))

                        } else if (type == "LineString") {
                            val list = repo.features[i].geometry.coordinates
                            for (k in list.indices) { // k
                                val lit = list[k].toString().split(" ".toRegex())
                                    .dropLastWhile { it.isEmpty() }.toTypedArray()
                                val lineX = lit[1].substring(0, lit[1].length - 1).toDouble()
                                val lineY = lit[0].substring(1, lit[0].length - 1).toDouble()

                                polyline.addPoint(MapPoint.mapPointWithGeoCoord(lineX, lineY))
                            }
                        }
                    }
                }

                val padding = 150 // px
                val mapPointBounds = MapPointBounds(polyline.mapPoints)
                mapView.addPolyline(polyline)
                mapView.moveCamera(
                    CameraUpdateFactory.newMapPointBounds(
                        mapPointBounds,
                        padding
                    )
                )
            }
        })
    }

    companion object {
        const val EXTRA_DATE = "EXTRA_DATE"
        const val EXTRA_SUGGEST_ROUTE_KEY = "EXTRA_SUGGEST_ROUTE_KEY"
    }
}

class PointDouble(lat: Double, lon: Double) {
    var lat: Double = lat
    var lon: Double = lon
}
