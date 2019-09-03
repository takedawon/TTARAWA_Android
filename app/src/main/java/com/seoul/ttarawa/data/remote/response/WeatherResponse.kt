package com.seoul.ttarawa.data.remote.response

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("response")
    val response: Response
) {
    data class Response(
        @SerializedName("body")
        val body: Body,
        @SerializedName("header")
        val header: Header
    ) {
        data class Body(
            @SerializedName("items")
            val items: Items,
            @SerializedName("numOfRows")
            val numOfRows: Int,
            @SerializedName("pageNo")
            val pageNo: Int,
            @SerializedName("totalCount")
            val totalCount: Int
        ) {
            data class Items(
                @SerializedName("item")
                val item: List<Item>
            ) {
                data class Item(
                    @SerializedName("baseDate")
                    val baseDate: Int,
                    @SerializedName("baseTime")
                    val baseTime: Int,
                    @SerializedName("category")
                    val category: String,
                    @SerializedName("fcstDate")
                    val fcstDate: Int,
                    @SerializedName("fcstTime")
                    val fcstTime: Int,
                    @SerializedName("fcstValue")
                    val fcstValue: Int,
                    @SerializedName("nx")
                    val nx: Int,
                    @SerializedName("ny")
                    val ny: Int
                )
            }
        }

        data class Header(
            @SerializedName("resultCode")
            val resultCode: String,
            @SerializedName("resultMsg")
            val resultMsg: String
        )
    }
}