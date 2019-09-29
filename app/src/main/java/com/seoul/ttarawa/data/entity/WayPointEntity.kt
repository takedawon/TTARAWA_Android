package com.seoul.ttarawa.data.entity

data class WayPointEntity(
    val name: String,
    val address: String,
    val lat: String,
    val lng: String,
    val startTime: String,
    val endTime: String,
    var isVisibleDelete: Boolean,
    val dayNumber: Int = 0
)