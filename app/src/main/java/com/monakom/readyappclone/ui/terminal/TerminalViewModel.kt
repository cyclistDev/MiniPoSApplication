package com.monakom.readyappclone.ui.terminal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.monakom.readyappclone.data.local.pref.SessionManager
import com.monakom.readyappclone.data.remote.dto.response.CompanyData
import com.monakom.readyappclone.data.remote.dto.response.TerminalData
import com.monakom.readyappclone.data.repository.AuthRepository
import kotlinx.coroutines.launch

class TerminalViewModel(application: Application) : AndroidViewModel(application) {

    private val authRepository = AuthRepository(application)

    // ── LiveData ──────────────────────────────────────────────────────────────
    private val _companies = MutableLiveData<List<CompanyData>>()
    val companies: LiveData<List<CompanyData>> get() = _companies

    private val _terminals = MutableLiveData<List<TerminalData>>()
    val terminals: LiveData<List<TerminalData>> get() = _terminals

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _connectSuccess = MutableLiveData<Boolean>()
    val connectSuccess: LiveData<Boolean> get() = _connectSuccess

    // ── Load companies ────────────────────────────────────────────────────────
    fun loadCompanies() {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Step 1: Get userId
                val userInfoResponse = authRepository.getUserInfo()
                if (userInfoResponse.isSuccessful && userInfoResponse.body() != null) {
                    val userId = userInfoResponse.body()!!.data.id
                    SessionManager.saveUserId(getApplication(), userId)

                    // Step 2: Get companies
                    val companyResponse = authRepository.getCompanies()
                    if (companyResponse.isSuccessful && companyResponse.body() != null) {
                        _companies.value = companyResponse.body()!!.data
                    } else {
                        _error.value = "Failed to load companies!"
                    }
                } else {
                    _error.value = "Failed to get user info!"
                }
            } catch (e: Exception) {
                _error.value = "Network error!"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Load terminals ────────────────────────────────────────────────────────
    fun loadTerminals(companyCode: String) {
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = authRepository.getTerminals(companyCode)
                if (response.isSuccessful && response.body() != null) {
                    _terminals.value = response.body()!!.data
                } else {
                    _error.value = "Failed to load terminals!"
                }
            } catch (e: Exception) {
                _error.value = "Network error!"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ── Save selected company ─────────────────────────────────────────────────
    fun selectCompany(company: CompanyData) {
        SessionManager.saveCompanyId(getApplication(), company.id)
        SessionManager.saveCompanyName(getApplication(), company.companyName)
        SessionManager.saveCompanyCode(getApplication(), company.companyCode)
        loadTerminals(company.companyCode)
    }

    // ── Save selected terminal ────────────────────────────────────────────────
    fun selectTerminal(terminal: TerminalData) {
        SessionManager.saveTerminalId(getApplication(), terminal.posTerminalId)
        checkConnectReady()
    }

    private fun checkConnectReady() {
        val companyId  = SessionManager.getCompanyId(getApplication())
        val terminalId = SessionManager.getTerminalId(getApplication())
        _connectSuccess.value = companyId != null && terminalId != null
    }
}