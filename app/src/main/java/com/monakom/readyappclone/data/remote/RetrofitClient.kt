package com.monakom.readyappclone.data.remote

import android.content.Context
import com.monakom.readyappclone.data.remote.api.AuthApi
//import com.monakom.readyappclone.data.remote.api.OrderApi
import com.monakom.readyappclone.data.remote.api.TicketApi
import com.monakom.readyappclone.data.remote.interceptor.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://uat.monakom.com/"

    private fun createOkHttpClient(context: Context): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))  // ← auto add token!
            .addInterceptor(logging)
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    private fun createRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(createOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // ← separate API instances
    fun authApi(context: Context): AuthApi =
        createRetrofit(context).create(AuthApi::class.java)

    fun ticketApi(context: Context): TicketApi =
        createRetrofit(context).create(TicketApi::class.java)

//    fun orderApi(context: Context): OrderApi =
//        createRetrofit(context).create(OrderApi::class.java)
}