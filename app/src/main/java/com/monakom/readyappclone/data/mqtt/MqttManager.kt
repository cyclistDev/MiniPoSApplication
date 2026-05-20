package com.monakom.readyappclone.data.mqtt

import android.util.Log
import com.monakom.readyappclone.BuildConfig
import com.monakom.readyappclone.utils.constants.AppConstants
import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttAsyncClient
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties

object MqttManager {

    private const val TAG = "MqttManager"

    private val BROKER_URL = "tcp://${BuildConfig.MQTT_URL}:${BuildConfig.MQTT_PORT}"
    private val USERNAME   = BuildConfig.MQTT_USERNAME
    private val PASSWORD   = BuildConfig.MQTT_PASSWORD

    private var mqttClient: MqttAsyncClient? = null

    var onMessageReceived: ((String) -> Unit)? = null

    fun connectMqtt(terminalId: String) {
        try {
            val clientId = "ReadyApp_${System.currentTimeMillis()}"
            mqttClient = MqttAsyncClient(BROKER_URL, clientId, MemoryPersistence())

            val options = org.eclipse.paho.mqttv5.client.MqttConnectionOptions().apply {
                userName = USERNAME
                password = PASSWORD.toByteArray()
                isCleanStart = true
                connectionTimeout = 30
                keepAliveInterval = 60
            }

            mqttClient?.setCallback(object : MqttCallback {
                override fun disconnected(disconnectResponse: MqttDisconnectResponse?) {
                    Log.e(TAG, "Disconnected: ${disconnectResponse?.exception?.message}")
                    // Auto reconnect
                    Thread.sleep(5000)
                    connectMqtt(terminalId)
                }

                override fun mqttErrorOccurred(exception: MqttException?) {
                    Log.e(TAG, "Error: ${exception?.message}")
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val payload = message?.payload?.toString(Charsets.UTF_8) ?: return
                    Log.d(TAG, "Message received on $topic: $payload")
                    onMessageReceived?.invoke(payload)
                }

                override fun deliveryComplete(token: IMqttToken?) {}

                override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                    Log.d(TAG, "Connected to MQTT broker! ✅")
                    subscribe(terminalId)
                }

                override fun authPacketArrived(reasonCode: Int, properties: MqttProperties?) {}
            })

            mqttClient?.connect(options)
            Log.d(TAG, "Connecting to MQTT broker...")

        } catch (e: Exception) {
            Log.e(TAG, "MQTT error: ${e.message}")
        }
    }

    private fun subscribe(terminalId: String) {
        val topic = "${AppConstants.MQTT_TOPIC_PREFIX}$terminalId"
        Log.d(TAG, "Subscribing to: $topic")

        try {
            mqttClient?.subscribe(topic, 1)
            Log.d(TAG, "Subscribed to: $topic ✅")
        } catch (e: Exception) {
            Log.e(TAG, "Subscribe failed: ${e.message}")
        }
    }

    fun disconnect() {
        try {
            mqttClient?.disconnect()
            mqttClient = null
            Log.d(TAG, "Disconnected!")
        } catch (e: Exception) {
            Log.e(TAG, "Disconnect error: ${e.message}")
        }
    }
}