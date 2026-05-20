package com.monakom.readyappclone.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.data.repository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    // ── LiveData ──────────────────────────────────────────────────────────────
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> get() = _loginSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // ── Login ─────────────────────────────────────────────────────────────────
    fun login(clientId: String, clientSecret: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = authRepository.login(clientId, clientSecret)

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!

                    // Save token
                    SessionManager.saveToken(
                        getApplication(),
                        body.accessToken,
                        body.refreshToken
                    )

                    _loginSuccess.value = true

                } else {
                    _error.value = "Invalid credentials!"
                }

            } catch (e: Exception) {
                _error.value = "Network error!"
            } finally {
                _isLoading.value = false
            }
        }
    }
}