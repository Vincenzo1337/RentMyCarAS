package com.example.rentmycaras.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Car
import kotlinx.coroutines.launch

class CarDetailViewModel(private val loginViewModel: LoginViewModel, savedStateHandle: SavedStateHandle): ViewModel() {

    private val _car: MutableLiveData<Car> = MutableLiveData()
    val car: MutableLiveData<Car> = _car

    private val _reservationSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val reservationSuccess: MutableLiveData<Boolean> = _reservationSuccess

    fun getCar(carId: String) {
        viewModelScope.launch {
            val carIdInt = carId.toIntOrNull()
            if (carIdInt != null) {
                val car = CarApi.carApiService.getCarById(carIdInt)
                if (car.isSuccessful) {
                    _car.value = car.body()
                }
            }
        }
    }


    fun updateReservationSuccess(success: Boolean) {
        _reservationSuccess.value = success
    }

    fun clearReservationSuccess() {
        _reservationSuccess.value = null
    }

    class CarDetailViewModelFactory(
        private val loginViewModel: LoginViewModel,
        private val savedStateHandle: SavedStateHandle
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CarDetailViewModel(loginViewModel, savedStateHandle) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
