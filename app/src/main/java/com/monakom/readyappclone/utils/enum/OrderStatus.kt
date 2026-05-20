package com.monakom.readyappclone.utils.enum

enum class OrderStatus(val value: String) {
    PREPARING("PREPARING"),
    READY("READY"),
    RE_CALL("RE_CALL"),
    DONE("DONE"),
    COMPLETED("COMPLETED"),
    UNKNOWN("UNKNOWN");

    companion object {
        fun fromValue(value: String): OrderStatus {
            return entries.find { it.value == value } ?: UNKNOWN
        }
    }
}