package com.monakom.readyappclone.data.remote.api

import com.monakom.readyappclone.data.remote.dto.response.OrderTypeResponse
import com.monakom.readyappclone.data.remote.dto.response.TicketListResponse
import retrofit2.Response
import retrofit2.http.*

interface TicketApi {

    @GET("gateway/adm/v1/api/queue_display/tickets/order-type")
    suspend fun getOrderTypes(
//        @Header("Authorization") token: String,
        @Query("terminalId") terminalId: String
    ): Response<OrderTypeResponse>

    @GET("gateway/adm/v1/api/queue_display/tickets/list")
    suspend fun getTicketList(
//        @Header("Authorization") token: String,
        @Query("terminalId") terminalId: String,
        @Query("orderType") orderType: String? = null
    ): Response<TicketListResponse>
}