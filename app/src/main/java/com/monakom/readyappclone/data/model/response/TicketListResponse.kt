package com.monakom.readyappclone.data.model.response

import com.google.gson.annotations.SerializedName

data class TicketListResponse(
    @SerializedName("code")    val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data")    val data: List<TicketData>,
    @SerializedName("total")   val total: Int
)

data class TicketData(
    @SerializedName("id")              val id: String,
    @SerializedName("ticketNumber")    val ticketNumber: String,       // ← was ticketNo
    @SerializedName("totalQty")        val totalQty: Int,              // ← was quantity
    @SerializedName("orderStatus")     val orderStatus: String,        // ← was status (String!)
    @SerializedName("orderType")       val orderType: String,
    @SerializedName("destinationName") val destinationName: String,    // ← was source
    @SerializedName("invoiceNo")       val invoiceNo: String,
    @SerializedName("terminalCode")    val terminalCode: String,
    @SerializedName("dateCreated")     val dateCreated: String,        // ← was createdAt
    @SerializedName("status")         val status: Boolean,             // ← boolean!
    @SerializedName("timeoutTime")     val timeoutTime: String?,
    @SerializedName("estimateDate")    val estimateDate: String?,
    @SerializedName("recallDate")      val recallDate: String?,
    @SerializedName("readyDate")       val readyDate: String?,
    @SerializedName("finishedIn")      val finishedIn: String?,
    @SerializedName("refNumber")       val refNumber: String?,
    @SerializedName("terminalId")      val terminalId: String?
)