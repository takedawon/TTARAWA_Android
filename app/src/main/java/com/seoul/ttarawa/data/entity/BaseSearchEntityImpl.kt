package com.seoul.ttarawa.data.entity

data class BaseSearchEntityImpl(
    override val categoryCode: Int,
    override val startTime: String,
    override val endTime: String,
    override val latitude: Double,
    override val longitude: Double,
    override val title: String,
    override val address: String,
    override val content: String
): BaseSearchEntity
