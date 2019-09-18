package com.seoul.ttarawa.data.entity

data class PathEntity(
    val name: String,
    val lat: String,
    val lng: String,
    val startTime: String,
    val endTime: String,
    var isVisibleDelete: Boolean
)