package com.monakom.readyappclone.data.remote

import com.monakom.readyappclone.data.model.request.LoginRequest
import com.monakom.readyappclone.data.model.response.CompanyResponse
import com.monakom.readyappclone.data.model.response.LoginResponse
import com.monakom.readyappclone.data.model.response.OrderTypeResponse
import com.monakom.readyappclone.data.model.response.TerminalResponse
import com.monakom.readyappclone.data.model.response.TicketListResponse
import com.monakom.readyappclone.data.model.response.UserInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // Login
    @POST("216/erp_cloud//adm/v1/api/oauth2")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    // Get user info → to get userId
    @GET("216/erp_cloud//adm/v1/api/user/info")
    suspend fun getUserInfo(
        @Header("Authorization") token: String
    ): Response<UserInfoResponse>

    // Get company list
    @GET("216/erp_cloud//adm/v1/api/user/user-company/{userId}")
    suspend fun getCompanies(
        @Header("Authorization") token: String,
        @Path("userId") userId: String
    ): Response<CompanyResponse>

    // Get terminal list by company
    @GET("216/erp_cloud//adm/v1/api/user/user-td-terminal/{userId}")
    suspend fun getTerminals(
        @Header("Authorization") token: String,
        @Path("userId") userId: String,
        @Query("companyCode") companyCode: String    // ← add this
    ): Response<TerminalResponse>


    // Get order types for tabs
    @GET("gateway/adm/v1/api/queue_display/tickets/order-type")
    suspend fun getOrderTypes(
        @Header("Authorization") token: String,
        @Query("terminalId") terminalId: String
    ): Response<OrderTypeResponse>

    // Get ticket list with optional filter
    @GET("gateway/adm/v1/api/queue_display/tickets/list")
    suspend fun getTicketList(
        @Header("Authorization") token: String,
        @Query("terminalId") terminalId: String,
//        @Query("orderType") orderType: String? = null
    ): Response<TicketListResponse>

}
