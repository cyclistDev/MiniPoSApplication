package com.monakom.readyappclone.data.repository

import android.content.Context
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.data.remote.RetrofitClient
import com.monakom.readyappclone.data.remote.dto.response.OrderTypeResponse
import com.monakom.readyappclone.data.remote.dto.response.TicketListResponse
import retrofit2.Response

class TicketRepository(private val context: Context) {

    private val ticketApi = RetrofitClient.ticketApi(context)

    suspend fun getOrderTypes(): Response<OrderTypeResponse> {
        val terminalId = SessionManager.getTerminalId(context) ?: ""
        return ticketApi.getOrderTypes(terminalId = terminalId)
    }

    suspend fun getTicketList(orderType: String? = null): Response<TicketListResponse> {
        val terminalId = SessionManager.getTerminalId(context) ?: ""
        return ticketApi.getTicketList(
            terminalId = terminalId,
            orderType = orderType
        )
    }
}