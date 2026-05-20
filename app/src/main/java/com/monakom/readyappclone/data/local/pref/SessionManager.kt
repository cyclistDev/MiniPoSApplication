package com.monakom.readyappclone.data.local.pref

import android.content.Context
import android.content.SharedPreferences
import com.monakom.readyappclone.utils.constants.AppConstants
import androidx.core.content.edit

object SessionManager {

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(AppConstants.PREF_SESSION, Context.MODE_PRIVATE)

    fun saveToken(context: Context, accessToken: String, refreshToken: String) {
        prefs(context).edit {
            putString(AppConstants.KEY_TOKEN, "Bearer $accessToken")
                .putString(AppConstants.KEY_REFRESH_TOKEN, refreshToken)
        }
    }

    fun getToken(context: Context): String? =
        prefs(context).getString(AppConstants.KEY_TOKEN, null)

    fun saveUserId(context: Context, userId: String) {
        prefs(context).edit { putString(AppConstants.KEY_USER_ID, userId) }
    }

    fun getUserId(context: Context): String? =
        prefs(context).getString(AppConstants.KEY_USER_ID, null)

    fun saveCompanyId(context: Context, companyId: String) {
        prefs(context).edit().putString(AppConstants.KEY_COMPANY_ID, companyId).apply()
    }

    fun getCompanyId(context: Context): String? =
        prefs(context).getString(AppConstants.KEY_COMPANY_ID, null)

    fun saveCompanyName(context: Context, companyName: String) {
        prefs(context).edit().putString(AppConstants.KEY_COMPANY_NAME, companyName).apply()
    }

    fun getCompanyName(context: Context): String? =
        prefs(context).getString(AppConstants.KEY_COMPANY_NAME, null)

    fun saveCompanyCode(context: Context, companyCode: String) {
        prefs(context).edit().putString(AppConstants.KEY_COMPANY_CODE, companyCode).apply()
    }

    fun getCompanyCode(context: Context): String? =
        prefs(context).getString(AppConstants.KEY_COMPANY_CODE, null)

    fun saveTerminalId(context: Context, terminalId: String) {
        prefs(context).edit().putString(AppConstants.KEY_TERMINAL_ID, terminalId).apply()
    }

    fun getTerminalId(context: Context): String? =
        prefs(context).getString(AppConstants.KEY_TERMINAL_ID, null)

    fun clearSession(context: Context) {
        prefs(context).edit().clear().apply()
    }
}