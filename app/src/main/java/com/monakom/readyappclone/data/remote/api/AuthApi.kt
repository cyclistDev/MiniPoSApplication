package com.monakom.readyappclone.data.remote.api

import com.monakom.readyappclone.data.remote.dto.request.LoginRequest
import com.monakom.readyappclone.data.remote.dto.response.LoginResponse
import com.monakom.readyappclone.data.remote.dto.response.UserInfoResponse
import com.monakom.readyappclone.data.remote.dto.response.CompanyResponse
import com.monakom.readyappclone.data.remote.dto.response.TerminalResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @POST("216/erp_cloud//adm/v1/api/oauth2")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("216/erp_cloud//adm/v1/api/user/info")
    suspend fun getUserInfo(
//        @Header("Authorization") token: String
    ): Response<UserInfoResponse>

    @GET("216/erp_cloud//adm/v1/api/user/user-company/{userId}")
    suspend fun getCompanies(
//        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<CompanyResponse>

    @GET("216/erp_cloud//adm/v1/api/user/user-td-terminal/{userId}")
    suspend fun getTerminals(
//        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("companyCode") companyCode: String
    ): Response<TerminalResponse>
}