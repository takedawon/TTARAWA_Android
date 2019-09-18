package com.seoul.ttarawa.data.remote.api

import com.seoul.ttarawa.BuildConfig
import retrofit2.http.GET

interface KobisApi {

    /**
     * @param key    (필수)	발급받은키 값을 입력합니다.
     * @param targetDt    (필수)	조회하고자 하는 날짜를 yyyymmdd 형식으로 입력합니다.
     * @param itemPerPage    결과 ROW 의 개수를 지정합니다.(default : “10”, 최대 : “10“)
     * @param multiMovieYn    다양성 영화/상업영화를 구분지어 조회할 수 있습니다. “Y” : 다양성 영화 “N” : 상업영화 (default : 전체)
     * @param repNationCd    한국/외국 영화별로 조회할 수 있습니다. “K: : 한국영화 “F” : 외국영화 (default : 전체)
     * @param wideAreaCd    상영지역별로 조회할 수 있으며, 지역코드는 공통코드 조회 서비스에서 “0105000000” 로서 조회된 지역코드입니다. (default : 전체)
     */
    @GET("boxoffice/searchDailyBoxOfficeList.json")
    fun getBoxOfficeDaily(
        key: String = BuildConfig.KOBIS_KEY,
        targetDt: String,
        itemPerPage: String = "10",
        multiMovieYn: String = "",
        repNationCd: String = "",
        wideAreaCd: String = ""
    )

    /**
     * 공통코드 조회
     *
     * @param key (필수)	발급받은키 값을 입력합니다.
     * @param comCode (필수)	조회하고자 하는 상위 코드를 입력합니다. "0105000000" 지역코드, "2204" 국적코드, "2201" 영화유형코드
     */
    @GET("code/searchCodeList.json")
    fun getCode(
        key: String = BuildConfig.KOBIS_KEY,
        comCode: String = "0105000000"
    )

    /**
     * 영화리스트
     * @param key    문자열(필수)	발급받은키 값을 입력합니다.
     * @param curPage    문자열	현재 페이지를 지정합니다.(default : “1”)
     * @param itemPerPage    문자열	결과 ROW 의 개수를 지정합니다.(default : “10”)
     * @param movieNm    문자열	영화명으로 조회합니다. (UTF-8 인코딩)
     * @param directorNm    문자열	감독명으로 조회합니다. (UTF-8 인코딩)
     * @param openStartDt    문자열	YYYY형식의 조회시작 개봉연도를 입력합니다.
     * @param openEndDt    문자열	YYYY형식의 조회종료 개봉연도를 입력합니다.
     * @param prdtStartYear    문자열 YYYY형식의 조회시작 제작연도를 입력합니다.
     * @param prdtEndYear    문자열 YYYY형식의 조회종료 제작연도를 입력합니다.
     * @param repNationCd    문자열	N개의 국적으로 조회할 수 있으며, 국적코드는 공통코드 조회 서비스에서 “2204” 로서 조회된 국적코드입니다. (default : 전체)
     * @param movieTypeCd    문자열 N개의 영화유형코드로 조회할 수 있으며, 영화유형코드는 공통코드 조회 서비스에서 “2201”로서 조회된 영화유형코드입니다. (default: 전체)
     */
    @GET("movie/searchMovieList.json")
    fun getMovieList(
        key: String = BuildConfig.KOBIS_KEY,
        curPage: String = "",
        itemPerPage: String = "",
        movieNm: String = "",
        directorNm: String = "",
        openStartDt: String = "",
        openEndDt: String = "",
        prdtStartYear: String = "",
        prdtEndYear: String = "",
        repNationCd: String = "",
        movieTypeCd: String = ""
    )
}