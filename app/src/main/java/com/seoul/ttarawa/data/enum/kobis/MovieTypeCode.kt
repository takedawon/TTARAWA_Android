package com.seoul.ttarawa.data.enum.kobis

enum class MovieTypeCode(
    val fullCd: String,
    val korNm: String
) {
    ALL("", "전체"),
    FEATURE("220101", "장편"),
    SHORT("220102", "단편"),
    OMNIBUS("220103", "옴니버스"),
    ETC("220199", "기타");
}