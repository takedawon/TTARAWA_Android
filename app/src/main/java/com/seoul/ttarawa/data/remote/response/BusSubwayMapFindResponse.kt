package com.seoul.ttarawa.data.remote.response


import com.google.gson.annotations.SerializedName

data class BusSubwayMapFindResponse(
    @SerializedName("result")
    val result: Result
) {
    data class Result(
        @SerializedName("busCount")
        val busCount: Int,
        @SerializedName("endRadius")
        val endRadius: Int,
        @SerializedName("outTrafficCheck")
        val outTrafficCheck: Int,
        @SerializedName("path")
        val path: List<Path>,
        @SerializedName("pointDistance")
        val pointDistance: Int,
        @SerializedName("searchType")
        val searchType: Int,
        @SerializedName("startRadius")
        val startRadius: Int,
        @SerializedName("subwayBusCount")
        val subwayBusCount: Int,
        @SerializedName("subwayCount")
        val subwayCount: Int
    ) {
        data class Path(
            @SerializedName("info")
            val info: Info,
            @SerializedName("pathType")
            val pathType: Int,
            @SerializedName("subPath")
            val subPath: List<SubPath>
        ) {
            data class Info(
                @SerializedName("busStationCount")
                val busStationCount: Int,
                @SerializedName("busTransitCount")
                val busTransitCount: Int,
                @SerializedName("firstStartStation")
                val firstStartStation: String,
                @SerializedName("lastEndStation")
                val lastEndStation: String,
                @SerializedName("mapObj")
                val mapObj: String,
                @SerializedName("payment")
                val payment: Int,
                @SerializedName("subwayStationCount")
                val subwayStationCount: Int,
                @SerializedName("subwayTransitCount")
                val subwayTransitCount: Int,
                @SerializedName("totalDistance")
                val totalDistance: Int,
                @SerializedName("totalStationCount")
                val totalStationCount: Int,
                @SerializedName("totalTime")
                val totalTime: Int,
                @SerializedName("totalWalk")
                val totalWalk: Int,
                @SerializedName("totalWalkTime")
                val totalWalkTime: Int,
                @SerializedName("trafficDistance")
                val trafficDistance: Int
            )

            data class SubPath(
                @SerializedName("distance")
                val distance: Int,
                @SerializedName("endID")
                val endID: Int,
                @SerializedName("endName")
                val endName: String,
                @SerializedName("endX")
                val endX: Double,
                @SerializedName("endY")
                val endY: Double,
                @SerializedName("lane")
                val lane: List<Lane>,
                @SerializedName("passStopList")
                val passStopList: PassStopList,
                @SerializedName("sectionTime")
                val sectionTime: Int,
                @SerializedName("startID")
                val startID: Int,
                @SerializedName("startName")
                val startName: String,
                @SerializedName("startX")
                val startX: Double,
                @SerializedName("startY")
                val startY: Double,
                @SerializedName("stationCount")
                val stationCount: Int,
                @SerializedName("trafficType")
                val trafficType: Int
            ) {
                data class Lane(
                    @SerializedName("busID")
                    val busID: Int,
                    @SerializedName("busNo")
                    val busNo: String,
                    @SerializedName("type")
                    val type: Int
                )

                data class PassStopList(
                    @SerializedName("stations")
                    val stations: List<Station>
                ) {
                    data class Station(
                        @SerializedName("index")
                        val index: Int,
                        @SerializedName("stationID")
                        val stationID: Int,
                        @SerializedName("stationName")
                        val stationName: String,
                        @SerializedName("x")
                        val x: String,
                        @SerializedName("y")
                        val y: String
                    )
                }
            }
        }
    }
}