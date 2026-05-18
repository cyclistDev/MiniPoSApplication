package com.monakom.readyappclone.data.mqtt

import android.util.Log
import com.monakom.readyappclone.BuildConfig
import com.hivemq.client.mqtt.MqttClient
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

object MqttManager {

    private const val TAG = "MqttManager"

    private const val HOST = BuildConfig.MQTT_URL
    private const val PORT = BuildConfig.MQTT_PORT
    private const val USERNAME = BuildConfig.MQTT_USERNAME
    private const val PASSWORD = BuildConfig.MQTT_PASSWORD

    private var client: Mqtt5AsyncClient? = null

    var onMessageReceived: ((String) -> Unit)? = null

    fun connect(terminalId: String) {

        try {
            client = MqttClient.builder()
                .useMqttVersion5()
                .serverHost(HOST)
                .serverPort(PORT)
                .buildAsync()

            client?.connectWith()
                ?.simpleAuth()
                ?.username(USERNAME)
                ?.password(ByteBuffer.wrap(PASSWORD.toByteArray()))
                ?.applySimpleAuth()
                ?.send()
                ?.whenComplete { _, throwable ->
                    if (throwable != null) {
                        Log.e(TAG, "Connection failed: ${throwable.message}")
                    } else {
                        Log.d(TAG, "Connected to MQTT broker!")
                        subscribe(terminalId)
                    }
                }

        } catch (e: Exception) {
            Log.e(TAG, "MQTT error: ${e.message}")
        }
    }

    private fun subscribe(terminalId: String) {

        val topic = "TicketService_uat_TicketReadyBroadcast_$terminalId"

        Log.d(TAG, "SUBSCRIBING TO: $topic")

        client?.subscribeWith()
            ?.topicFilter(topic)
            ?.qos(MqttQos.AT_LEAST_ONCE)
            ?.callback { publish: Mqtt5Publish ->
                val message = StandardCharsets.UTF_8
                    .decode(publish.payload.get())
                    .toString()

                Log.d(TAG, "Message received: $message")
                onMessageReceived?.invoke(message)
            }
            ?.send()
            ?.whenComplete { _, throwable ->
                if (throwable != null) {
                    Log.e(TAG, "Subscribe failed: ${throwable.message}")
                } else {
                    Log.d(TAG, "Subscribed to: $topic")
                }
            }
    }

    fun disconnect() {
        client?.disconnect()
        client = null
        Log.d(TAG, "Disconnected from MQTT")
    }
}