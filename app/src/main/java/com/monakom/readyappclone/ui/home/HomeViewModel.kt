package com.monakom.readyappclone.ui.home

import androidx.lifecycle.*
import com.monakom.readyappclone.data.model.response.TicketData
import com.monakom.readyappclone.data.remote.RetrofitClient
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _tickets = MutableLiveData<List<TicketData>>()
    val tickets: LiveData<List<TicketData>> = _tickets

    private val _orderTypes = MutableLiveData<List<String>>()
    val orderTypes: LiveData<List<String>> = _orderTypes

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadOrderTypes(token: String, terminalId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getOrderTypes(
                    token = token,
                    terminalId = terminalId
                )

                if (response.isSuccessful && response.body() != null) {
                    val types = response.body()!!.data.distinct()
                    _orderTypes.postValue(types)
                } else {
                    _error.postValue("Failed to load tabs")
                }

            } catch (e: Exception) {
                _error.postValue("Network error")
            }
        }
    }

    fun loadTickets(token: String, terminalId: String, orderType: String?) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getTicketList(
                    token = token,
                    terminalId = terminalId,
//                    orderType = orderType
                )

                if (response.isSuccessful && response.body() != null) {
                    _tickets.postValue(response.body()!!.data)
                } else {
                    _error.postValue("Failed to load tickets")
                }

            } catch (e: Exception) {
                _error.postValue("Network error")
            }
        }
    }
}