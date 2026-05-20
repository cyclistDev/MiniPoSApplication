package com.monakom.readyappclone.utils.constants

object AppConstants {

    // MQTT
    const val MQTT_TOPIC_PREFIX = "TicketService_uat_TicketReadyBroadcast_"

    // Session keys
    const val PREF_SESSION   = "session_prefs"
    const val KEY_TOKEN      = "access_token"
    const val KEY_REFRESH_TOKEN    = "refresh_token"
    const val KEY_USER_ID    = "user_id"
    const val KEY_COMPANY_ID = "company_id"
    const val KEY_COMPANY_NAME = "company_name"
    const val KEY_COMPANY_CODE = "company_code"
    const val KEY_TERMINAL_ID  = "pos_terminal_id"

    // Locale
    const val PREF_LOCALE    = "app_prefs"
    const val KEY_LANGUAGE   = "language"
    const val LANG_EN        = "en"
    const val LANG_KM        = "km"
    const val LANG_ZH        = "zh"

    // Auto refresh
    const val REFRESH_INTERVAL = 30000L // 30 seconds
}