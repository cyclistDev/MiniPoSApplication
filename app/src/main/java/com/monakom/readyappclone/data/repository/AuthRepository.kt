package com.monakom.readyappclone.data.repository

import android.content.Context
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.data.remote.RetrofitClient
import com.monakom.readyappclone.data.remote.dto.request.LoginRequest
import com.monakom.readyappclone.data.remote.dto.response.CompanyResponse
import com.monakom.readyappclone.data.remote.dto.response.LoginResponse
import com.monakom.readyappclone.data.remote.dto.response.TerminalResponse
import com.monakom.readyappclone.data.remote.dto.response.UserInfoResponse
import retrofit2.Response

class AuthRepository(private val context: Context) {

    private val authApi = RetrofitClient.authApi(context)

    suspend fun login(clientId: String, clientSecret: String): Response<LoginResponse> {
        return authApi.login(LoginRequest(clientId = clientId, clientSecret = clientSecret))
    }

    suspend fun getUserInfo(): Response<UserInfoResponse> {
        return authApi.getUserInfo()
    }

    suspend fun getCompanies(): Response<CompanyResponse> {
        val userId = SessionManager.getUserId(context) ?: ""
        return authApi.getCompanies(userId = userId)
    }

    suspend fun getTerminals(companyCode: String): Response<TerminalResponse> {
        val userId = SessionManager.getUserId(context) ?: ""
        return authApi.getTerminals(
            userId = userId,
            companyCode = companyCode
        )
    }
}