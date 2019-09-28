package com.seoul.ttarawa.data.entity

interface BaseSearchEntity {
    val categoryCode: Int
    val startTime: Int
    val endTime: Int
    val latitude: Double
    val longitude: Double
    val title: String
    val address: String
}
