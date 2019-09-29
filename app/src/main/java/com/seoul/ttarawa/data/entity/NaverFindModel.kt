package com.seoul.ttarawa.data.entity

import java.io.Serializable

data class NaverFindModel(
    override val categoryCode: Int,
    override val startTime: Int,
    override val endTime: Int,
    override val latitude: Double,
    override val longitude: Double,
    override val title: String,
    override val address: String
):BaseSearchEntity, Serializable