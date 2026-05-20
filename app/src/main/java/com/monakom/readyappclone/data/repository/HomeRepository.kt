//package com.monakom.readyappclone.data.repository
//
//import com.monakom.readyappclone.data.remote.api.OrderApi
//import com.monakom.readyappclone.data.remote.api.TicketApi
//
//class HomeRepository(
//    private val ticketApi: TicketApi,
//    private val orderApi: OrderApi
//) {
//
//    suspend fun getTickets(token: String, terminalId: String, type: String?) =
//        ticketApi.getTicketList(token, terminalId)
//
//    suspend fun getOrderTypes(token: String, terminalId: String) =
//        orderApi.getOrderTypes(token, terminalId)
//}