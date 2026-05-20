//package com.monakom.readyappclone.data.remote.api
//
//import com.monakom.readyappclone.data.remote.dto.response.CompanyResponse
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Header
//import retrofit2.http.Path
//
//interface CompanyApi {
//    @GET("216/erp_cloud//adm/v1/api/user/user-company/{userId}")
//    suspend fun getCompanies(
//        @Header("Authorization") token: String,
//        @Path("userId") userId: String
//    ): Response<CompanyResponse>
//}