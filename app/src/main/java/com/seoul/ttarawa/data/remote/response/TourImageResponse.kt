package com.seoul.ttarawa.data.remote.response


import com.google.gson.annotations.SerializedName

data class TourImageResponse(
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
                    @SerializedName("contentid")
                    val contentid: Int,
                    @SerializedName("originimgurl")
                    val originimgurl: String,
                    @SerializedName("serialnum")
                    val serialnum: String,
                    @SerializedName("smallimageurl")
                    val smallimageurl: String
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