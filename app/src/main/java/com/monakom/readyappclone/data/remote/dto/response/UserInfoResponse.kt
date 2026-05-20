package com.monakom.readyappclone.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("code")    val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data")    val data: UserData
)

data class UserData(
    @SerializedName("id")       val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("name")     val name: String,
    @SerializedName("email")    val email: String,
    @SerializedName("status")   val status: Boolean
)