package com.monakom.readyappclone.data.remote.interceptor

import com.monakom.readyappclone.utils.constants.AppConstants
import okhttp3.Interceptor
import okhttp3.Response
import android.content.Context
import android.content.SharedPreferences

/**
 * AuthInterceptor automatically adds Authorization header
 * to every API request — no need to pass token manually each time!
 *
 * Before:
 * suspend fun getTickets(@Header("Authorization") token: String)
 *
 * After:
 * suspend fun getTickets() ← token added automatically!
 */
class AuthInterceptor(context: Context) : Interceptor {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(AppConstants.PREF_SESSION, Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = prefs.getString(AppConstants.KEY_TOKEN, null)

        val request = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", token)
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(request)
    }
}