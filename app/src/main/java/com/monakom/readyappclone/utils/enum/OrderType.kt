package com.monakom.readyappclone.utils.enum

enum class OrderType(val value: String) {
    ALL("All"),
    COUNTER("Counter"),
    MOBILE("Mobile"),
    PICKUP("Pickup"),
    DELIVERY("Delivery"),
    DINE_IN("DineIn"),
    DIGITAL("Digital"),
    WALK_IN("Walk In"),
    NEW("New"),
    UNKNOWN("Unknown");

    companion object {
        fun fromValue(value: String): OrderType {
            return entries.find { it.value == value } ?: UNKNOWN
        }
    }
}