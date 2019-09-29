package com.seoul.ttarawa.data.entity

interface BaseSearchEntity {
    val categoryCode: Int
    val startTime: String
    val endTime: String
    val latitude: Double
    val longitude: Double
    val title: String
    val address: String
    val content: String
}
