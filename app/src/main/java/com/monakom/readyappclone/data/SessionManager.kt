package com.monakom.readyappclone.data

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "session_prefs"
    private const val KEY_TOKEN = "access_token"
    private const val KEY_REFRESH_TOKEN = "refresh_token"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_COMPANY_ID = "company_id"
    private const val KEY_COMPANY_NAME = "company_name"
    private const val KEY_COMPANY_CODE = "company_code"
    private const val KEY_TERMINAL_ID = "pos_terminal_id"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveToken(context: Context, accessToken: String, refreshToken: String) {
        prefs(context).edit()
            .putString(KEY_TOKEN, "Bearer $accessToken")
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .apply()
    }

    fun getToken(context: Context): String? =
        prefs(context).getString(KEY_TOKEN, null)

    fun saveUserId(context: Context, userId: String) {
        prefs(context).edit().putString(KEY_USER_ID, userId).apply()
    }

    fun getUserId(context: Context): String? =
        prefs(context).getString(KEY_USER_ID, null)

    fun saveCompanyId(context: Context, companyId: String) {
        prefs(context).edit().putString(KEY_COMPANY_ID, companyId).apply()
    }

    fun getCompanyId(context: Context): String? =
        prefs(context).getString(KEY_COMPANY_ID, null)

    fun saveCompanyName(context: Context, companyName: String) {
        prefs(context).edit().putString(KEY_COMPANY_NAME, companyName).apply()
    }

    fun getCompanyName(context: Context): String? =
        prefs(context).getString(KEY_COMPANY_NAME, null)

    fun saveCompanyCode(context: Context, companyCode: String) {
        prefs(context).edit().putString(KEY_COMPANY_CODE, companyCode).apply()
    }

    fun getCompanyCode(context: Context): String? =
        prefs(context).getString(KEY_COMPANY_CODE, null)

    fun saveTerminalId(context: Context, terminalId: String) {
        prefs(context).edit().putString(KEY_TERMINAL_ID, terminalId).apply()
    }

    fun getTerminalId(context: Context): String? =
        prefs(context).getString(KEY_TERMINAL_ID, null)

    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }
}