package com.seoul.ttarawa.ui.search

import android.graphics.Color
import com.seoul.ttarawa.R

enum class CategoryType(
    val code: Int,
    val PathColor: Int,
    val pathStrokeColor: Int,
    val markerIconId: Int,
    val text: String
) {
    NONE_SELECTED(-1, R.color.black, R.color.black, 0, ""),
    MOVIE(0, android.R.color.holo_blue_bright, R.color.black, R.drawable.path_ic_movie_marker, "영화"),
    WAY_POINT(1, android.R.color.holo_blue_dark, R.color.black, R.drawable.path_ic_way_point_marker, "경유지"),
    CAFE(2, android.R.color.holo_green_dark, R.color.black, R.drawable.path_ic_cafe_marker, "카페"),
    CULTURE(3, android.R.color.holo_orange_dark, R.color.black, R.drawable.path_ic_culture_marker, "문화행사"),
    EXHIBITION(4, android.R.color.holo_red_dark, R.color.black, R.drawable.path_ic_exhibition_marker, "전시회"),
    TOUR(5, android.R.color.holo_purple, R.color.black, R.drawable.path_ic_tour_marker, "관광지"),
    SPORTS(6, android.R.color.holo_green_light, R.color.black, R.drawable.path_ic_sports_marker, "스포츠"),
    SHOPPING(7, android.R.color.holo_orange_light, R.color.black, R.drawable.path_ic_shopping_marker, "쇼핑");

    companion object {
        fun get(code: Int) =
            when (code) {
                -1 -> NONE_SELECTED
                0 -> MOVIE
                1 -> WAY_POINT
                2 -> CAFE
                3 -> CULTURE
                4 -> EXHIBITION
                5 -> TOUR
                6 -> SPORTS
                7 -> SHOPPING
                else -> WAY_POINT
            }
    }
}