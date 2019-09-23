package com.seoul.ttarawa.data.remote.response


import com.google.gson.annotations.SerializedName

data class TourDetailsResponse(
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
                val item: Item
            ) {
                data class Item(
                    @SerializedName("contentid")
                    val contentid: Int,
                    @SerializedName("contenttypeid")
                    val contenttypeid: Int,
                    @SerializedName("fldgubun")
                    val fldgubun: Int,
                    @SerializedName("infoname")
                    val infoname: String,
                    @SerializedName("infotext")
                    val infotext: String,
                    @SerializedName("serialnum")
                    val serialnum: Int
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