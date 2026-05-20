//package com.monakom.readyappclone.data.remote.api
//
//import com.monakom.readyappclone.data.remote.dto.response.UserInfoResponse
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Header
//
//interface UserInfoApi {
//    @GET("216/erp_cloud//adm/v1/api/user/info")
//    suspend fun getUserInfo(
//        @Header("Authorization") token: String
//    ): Response<UserInfoResponse>
//}