package com.seoul.ttarawa.data.entity

import android.os.Parcel
import android.os.Parcelable

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
) : BaseSearchEntity, Parcelable {

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readString() ?: "",
        source.readDouble(),
        source.readDouble(),
        source.readString(),
        source.readInt(),
        source.readInt(),
        source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(categoryCode)
        writeString(title)
        writeString(address)
        writeString(content)
        writeString(startTime)
        writeString(endTime)
        writeDouble(latitude)
        writeDouble(longitude)
        writeString(imgUrl)
        writeInt(contentId)
        writeInt(startDate)
        writeInt(endDate)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LocationTourModel> =
            object : Parcelable.Creator<LocationTourModel> {
                override fun createFromParcel(source: Parcel): LocationTourModel =
                    LocationTourModel(source)

                override fun newArray(size: Int): Array<LocationTourModel?> = arrayOfNulls(size)
            }
    }
}

