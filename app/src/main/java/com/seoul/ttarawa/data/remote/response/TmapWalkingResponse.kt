package com.seoul.ttarawa.data.remote.response


import com.google.gson.annotations.SerializedName

data class TmapWalkingResponse(
    @SerializedName("features")
    val features: List<Feature>,
    @SerializedName("type")
    val type: String
) {
    data class Feature(
        @SerializedName("geometry")
        val geometry: Geometry,
        @SerializedName("properties")
        val properties: Properties,
        @SerializedName("type")
        val type: String
    ) {
        data class Geometry(
            @SerializedName("coordinates")
            val coordinates: List<Any>,
            @SerializedName("type")
            val type: String
        )

        data class Properties(
            @SerializedName("description")
            val description: String,
            @SerializedName("direction")
            val direction: String,
            @SerializedName("facilityName")
            val facilityName: String,
            @SerializedName("facilityType")
            val facilityType: String,
            @SerializedName("index")
            val index: Int,
            @SerializedName("intersectionName")
            val intersectionName: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("nearPoiName")
            val nearPoiName: String,
            @SerializedName("nearPoiX")
            val nearPoiX: String,
            @SerializedName("nearPoiY")
            val nearPoiY: String,
            @SerializedName("pointIndex")
            val pointIndex: Int,
            @SerializedName("pointType")
            val pointType: String,
            @SerializedName("turnType")
            val turnType: Int
        )
    }

}