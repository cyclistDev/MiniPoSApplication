package com.monakom.readyappclone.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class TerminalResponse(
    @SerializedName("code")    val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data")    val data: List<TerminalData>
)

data class TerminalData(
    @SerializedName("id")           val id: String,
    @SerializedName("posTerminalId") val posTerminalId: String,
    @SerializedName("terminalName") val terminalName: String,
    @SerializedName("terminalType") val terminalType: String,
    @SerializedName("companyId")    val companyId: String,
    @SerializedName("storeName")    val storeName: String
)