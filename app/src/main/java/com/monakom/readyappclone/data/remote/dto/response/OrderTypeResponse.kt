package com.monakom.readyappclone.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class OrderTypeResponse(
    @SerializedName("code")    val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data")    val data: List<String>,
    @SerializedName("total")   val total: Int
)