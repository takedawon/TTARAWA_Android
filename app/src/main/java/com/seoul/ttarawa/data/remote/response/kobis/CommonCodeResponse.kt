package com.seoul.ttarawa.data.remote.response.kobis


import com.google.gson.annotations.SerializedName

data class CommonCodeResponse(
    @SerializedName("codes")
    val codes: List<Code>
) {

    /**
     * @property fullCd	문자열	상위코드를 출력합니다
     * @property korNm	문자열	해당 코드의 국문명을 출력합니다.
     * @property engNm	문자열	해당 코드의 영문명을 출력합니다.
     */
    data class Code(
        @SerializedName("engNm")
        val engNm: String,
        @SerializedName("fullCd")
        val fullCd: String,
        @SerializedName("korNm")
        val korNm: String
    )
}