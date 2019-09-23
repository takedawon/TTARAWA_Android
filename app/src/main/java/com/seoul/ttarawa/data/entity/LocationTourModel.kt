package com.seoul.ttarawa.data.entity

data class LocationTourModel(
    val imgUrl: String?,
    val title: String,
    val address: String,
    val contentID: Int,
    val mapX:Double,
    val mapY:Double,
    val startDate:Int,
    val endDate:Int
)
