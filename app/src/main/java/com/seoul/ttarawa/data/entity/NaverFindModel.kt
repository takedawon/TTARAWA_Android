package com.seoul.ttarawa.data.entity

import java.io.Serializable

data class NaverFindModel(
    override val categoryCode: Int,
    override val startTime: String,
    override val endTime: String,
    override val latitude: Double,
    override val longitude: Double,
    override val title: String,
    override val address: String,
    override val content: String
):BaseSearchEntity, Serializable