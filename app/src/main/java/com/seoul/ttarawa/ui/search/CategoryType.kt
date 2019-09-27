package com.seoul.ttarawa.ui.search

enum class CategoryType(val code: Int, val text: String) {
    NONE_SELECTED(-1, ""),
    MOVIE(0, "영화"),
    WAY_POINT(1, "경유지"),
    CAFE(2, "카페"),
    CULTURE(3, "문화행사"),
    EXHIBITION(4, "전시회"),
    TOUR(5, "관광지"),
    SPORTS(6, "스포츠"),
    SHOPPING(7, "쇼핑");

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