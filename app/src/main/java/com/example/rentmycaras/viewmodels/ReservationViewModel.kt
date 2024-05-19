package com.example.rentmycaras.viewmodels

import Reservation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.api.CarApi
import kotlinx.coroutines.launch

class ReservationViewModel(private val loginViewModel: LoginViewModel) : ViewModel() {
    private val _reservations = MutableLiveData<List<Reservation>>()
    val reservations: LiveData<List<Reservation>> get() = _reservations

    init {
        fetchReservations()
    }

    private fun fetchReservations() {
        viewModelScope.launch {
            val userid = loginViewModel.loggedInUserId.value
            if (userid != null) {
                val reservations = CarApi.carApiService.getReservationsByUser(userid)
                _reservations.value = reservations
            }
        }
    }


    class ReservationViewModelFactory(private val loginViewModel: LoginViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ReservationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ReservationViewModel(loginViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
