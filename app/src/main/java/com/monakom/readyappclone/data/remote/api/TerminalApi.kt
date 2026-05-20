//package com.monakom.readyappclone.data.remote.api
//
//import com.monakom.readyappclone.data.remote.dto.response.TerminalResponse
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Header
//import retrofit2.http.Path
//import retrofit2.http.Query
//
//interface TerminalApi {
//    @GET("216/erp_cloud//adm/v1/api/user/user-td-terminal/{userId}")
//    suspend fun getTerminals(
//        @Header("Authorization") token: String,
//        @Path("userId") userId: String,
//        @Query("companyCode") companyCode: String
//    ): Response<TerminalResponse>
//}