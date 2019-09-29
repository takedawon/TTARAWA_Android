package com.seoul.ttarawa.data.entity

data class WayPointEntity(
    val name: String,
    val address: String,
    val lat: Double,
    val lng: Double,
    val startTime: String,
    val endTime: String,
    var isVisibleDelete: Boolean = false,
    val dayNumber: Int = 0
)