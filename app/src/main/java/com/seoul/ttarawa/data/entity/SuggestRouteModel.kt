package com.seoul.ttarawa.data.entity

import com.seoul.ttarawa.data.local.entity.NodeEntity

data class SuggestRouteModel(
    val id: String = "",
    val imgUri: String = "",
    val title: String = "",
    val subTitle: String = "",
    val textColor: Int = 0,
    val date: String
)

data class SuggestRouteLeaf(
    val imgUri: String = "",
    val title: String = "",
    val shareYn: Boolean = false,
    val subTitle: String = "",
    val textColor: Int = 0,
    val nodes: List<NodeEntity> = emptyList(),
    val date: String = ""
) {
    fun toSuggestRouteModel(id: String) = SuggestRouteModel(
        id = id,
        imgUri = imgUri,
        title = title,
        subTitle = subTitle,
        textColor = textColor,
        date = date
    )
}
