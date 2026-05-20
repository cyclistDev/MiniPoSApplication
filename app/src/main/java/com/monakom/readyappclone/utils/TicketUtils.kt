package com.monakom.readyappclone.utils

import com.monakom.readyappclone.data.remote.dto.response.TicketData

object TicketUtils {

    fun filterByOrderType(
        tickets: List<TicketData>,
        orderType: String?
    ): List<TicketData> {

        return if (orderType == null) tickets
        else tickets.filter { it.orderType == orderType }
    }
}