package com.example.rentmycaras.viewmodels

import Reservation
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Car
import kotlinx.coroutines.launch

class ReservationViewModel(private val loginViewModel: LoginViewModel) : ViewModel() {
    private val _reservations = MutableLiveData<List<Reservation>>()
    val reservations: LiveData<List<Reservation>> get() = _reservations

    private val _cars = MutableLiveData<List<Car>>() // Nieuwe LiveData voor de auto-informatie
    val cars: LiveData<List<Car>> get() = _cars

    init {
        fetchReservations()
    }

    private fun fetchReservations() {
        viewModelScope.launch {
            val userid = loginViewModel.loggedInUserId.value
            if (userid != null) {
                val reservations = CarApi.carApiService.getReservationsByUser(userid)
                val cars = reservations.map { CarApi.carApiService.getCarById(it.carId).body() }
                _cars.value = cars.filterNotNull() // Filter out any null Car objects
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
