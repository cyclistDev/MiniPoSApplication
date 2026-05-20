package com.monakom.readyappclone.data.mqtt

interface MqttListener {

    fun onConnected()

    fun onMessageReceived(message: String)

    fun onError(error: Throwable)
}