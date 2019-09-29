package com.seoul.ttarawa.data.remote.response


import com.google.gson.annotations.SerializedName

data class NaverFindResponse(
    @SerializedName("display")
    val display: Int,
    @SerializedName("items")
    val items: List<Item>,
    @SerializedName("lastBuildDate")
    val lastBuildDate: String,
    @SerializedName("start")
    val start: Int,
    @SerializedName("total")
    val total: Int
) {
    data class Item(
        @SerializedName("address")
        val address: String,
        @SerializedName("category")
        val category: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("mapx")
        val mapx: String,
        @SerializedName("mapy")
        val mapy: String,
        @SerializedName("roadAddress")
        val roadAddress: String,
        @SerializedName("telephone")
        val telephone: String,
        @SerializedName("title")
        val title: String
    )
}