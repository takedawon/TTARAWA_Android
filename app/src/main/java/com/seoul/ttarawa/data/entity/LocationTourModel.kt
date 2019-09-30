package com.seoul.ttarawa.data.entity

import java.io.Serializable

data class LocationTourModel(
    override val categoryCode: Int,
    override val title: String,
    override val address: String,
    override val content: String,
    override val startTime: String,
    override val endTime: String,
    override val latitude: Double,
    override val longitude: Double,
    val imgUrl: String?,
    val contentId: Int,
    val startDate: Int,
    val endDate: Int
) : BaseSearchEntity, Serializable

