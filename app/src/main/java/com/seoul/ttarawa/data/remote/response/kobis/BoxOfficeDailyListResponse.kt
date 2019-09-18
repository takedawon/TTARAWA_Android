package com.seoul.ttarawa.data.remote.response.kobis


import com.google.gson.annotations.SerializedName

data class BoxOfficeDailyListResponse(
    @SerializedName("boxOfficeResult")
    val boxOfficeResult: BoxOfficeResult
) {

    /**
     * @property boxofficeType 박스오피스 종류를 출력합니다.
     * @property dailyBoxOfficeList
     * @property showRange 박스오피스 조회 일자를 출력합니다.
     */
    data class BoxOfficeResult(
        @SerializedName("boxofficeType")
        val boxofficeType: String,
        @SerializedName("dailyBoxOfficeList")
        val dailyBoxOfficeList: List<DailyBoxOffice>,
        @SerializedName("showRange")
        val showRange: String
    ) {
        /**
         * @property rnum    순번을 출력합니다.
         * @property rank    해당일자의 박스오피스 순위를 출력합니다.
         * @property rankInten    전일대비 순위의 증감분을 출력합니다.
         * @property rankOldAndNew    랭킹에 신규진입여부를 출력합니다. “OLD” : 기존 , “NEW” : 신규
         * @property movieCd    영화의 대표코드를 출력합니다.
         * @property movieNm    영화명(국문)을 출력합니다.
         * @property openDt    영화의 개봉일을 출력합니다.
         * @property salesAmt    해당일의 매출액을 출력합니다.
         * @property salesShare    해당일자 상영작의 매출총액 대비 해당 영화의 매출비율을 출력합니다.
         * @property salesInten    전일 대비 매출액 증감분을 출력합니다.
         * @property salesChange    전일 대비 매출액 증감 비율을 출력합니다.
         * @property salesAcc    누적매출액을 출력합니다.
         * @property audiCnt    해당일의 관객수를 출력합니다.
         * @property audiInten    전일 대비 관객수 증감분을 출력합니다.
         * @property audiChange    전일 대비 관객수 증감 비율을 출력합니다.
         * @property audiAcc    누적관객수를 출력합니다.
         * @property scrnCnt    해당일자에 상영한 스크린수를 출력합니다.
         * @property showCnt    해당일자에 상영된 횟수를 출력합니다.
         */
        data class DailyBoxOffice(
            @SerializedName("audiAcc")
            val audiAcc: String,
            @SerializedName("audiChange")
            val audiChange: String,
            @SerializedName("audiCnt")
            val audiCnt: String,
            @SerializedName("audiInten")
            val audiInten: String,
            @SerializedName("movieCd")
            val movieCd: String,
            @SerializedName("movieNm")
            val movieNm: String,
            @SerializedName("openDt")
            val openDt: String,
            @SerializedName("rank")
            val rank: String,
            @SerializedName("rankInten")
            val rankInten: String,
            @SerializedName("rankOldAndNew")
            val rankOldAndNew: String,
            @SerializedName("rnum")
            val rnum: String,
            @SerializedName("salesAcc")
            val salesAcc: String,
            @SerializedName("salesAmt")
            val salesAmt: String,
            @SerializedName("salesChange")
            val salesChange: String,
            @SerializedName("salesInten")
            val salesInten: String,
            @SerializedName("salesShare")
            val salesShare: String,
            @SerializedName("scrnCnt")
            val scrnCnt: String,
            @SerializedName("showCnt")
            val showCnt: String
        )
    }
}