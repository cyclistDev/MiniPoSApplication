package com.monakom.readyappclone.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class CompanyResponse(
    @SerializedName("code")    val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data")    val data: List<CompanyData>
)

data class CompanyData(
    @SerializedName("id")            val id: String,
    @SerializedName("companyCode")   val companyCode: String,
    @SerializedName("companyName")   val companyName: String,
    @SerializedName("companyNameKh") val companyNameKh: String?,
    @SerializedName("companyLogo")   val companyLogo: String?,
    @SerializedName("active")        val active: Boolean
)