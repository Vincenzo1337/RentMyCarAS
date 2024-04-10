package com.example.rentmycaras.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Car
import kotlinx.coroutines.launch

class CarDetailViewModel(savedStateHandle: SavedStateHandle): ViewModel() {
    private val carId: String = checkNotNull(savedStateHandle["carId"])

    private val _car: MutableLiveData<Car> = MutableLiveData()
    val car: MutableLiveData<Car> = _car

    fun getCar() {
        viewModelScope.launch {
            val car = CarApi.carApiService.getCarById(carId)
            if (car.isSuccessful) {
                _car.value = car.body()
            }
        }
    }
}