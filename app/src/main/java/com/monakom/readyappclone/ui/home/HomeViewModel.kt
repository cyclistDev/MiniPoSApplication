package com.monakom.readyappclone.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.monakom.readyappclone.data.remote.dto.response.TicketData
import com.monakom.readyappclone.data.repository.TicketRepository
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val ticketRepository = TicketRepository(application)

    // ── LiveData ──────────────────────────────────────────────────────────────
    private val _orderTypes = MutableLiveData<List<String>>()
    val orderTypes: LiveData<List<String>> get() = _orderTypes

    private val _tickets = MutableLiveData<List<TicketData>>()
    val tickets: LiveData<List<TicketData>> get() = _tickets

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // ── Current filter ────────────────────────────────────────────────────────
    var currentOrderType: String? = null
        private set

    // ── API calls ─────────────────────────────────────────────────────────────

    fun loadOrderTypes() {
        viewModelScope.launch {
            try {
                val response = ticketRepository.getOrderTypes()
                if (response.isSuccessful && response.body() != null) {
                    _orderTypes.value = response.body()!!.data.distinct()
                } else {
                    _error.value = "Failed to load tabs!"
                }
            } catch (e: Exception) {
                _error.value = "Network error!"
            }
        }
    }

    fun loadTickets(orderType: String? = null) {
        currentOrderType = orderType
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ticketRepository.getTicketList(orderType)
                if (response.isSuccessful && response.body() != null) {
                    val allTickets = response.body()!!.data

                    // Filter on frontend
                    val filtered = if (orderType == null) {
                        allTickets
                    } else {
                        allTickets.filter { it.orderType == orderType }
                    }
                    _tickets.value = filtered
                } else {
                    _error.value = "Failed to load tickets!"
                }
            } catch (e: Exception) {
                _error.value = "Network error!"
            } finally {
                _isLoading.value = false
            }
        }
    }
}