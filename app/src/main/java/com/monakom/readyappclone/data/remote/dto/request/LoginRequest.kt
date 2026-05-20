package com.monakom.readyappclone.data.remote.dto.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("client_id")     val clientId: String,
    @SerializedName("client_secret") val clientSecret: String,
    @SerializedName("grant_type")    val grantType: String = "client_credentials",
    @SerializedName("scope")         val scope: String = "mobile.pos.integration"
)