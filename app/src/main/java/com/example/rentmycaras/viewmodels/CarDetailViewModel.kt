package com.example.rentmycaras.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Car
import com.example.rentmycaras.models.CarCategory
import com.example.rentmycaras.models.Location
import com.example.rentmycaras.models.TimeBlock
import kotlinx.coroutines.launch

class CarDetailViewModel(private val loginViewModel: LoginViewModel) : ViewModel() {

    private val _car: MutableLiveData<Car> = MutableLiveData()
    val car: MutableLiveData<Car> = _car

    private val _reservationSuccess: MutableLiveData<Boolean?> = MutableLiveData()
    val reservationSuccess: LiveData<Boolean?> = _reservationSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _carAddedSuccess = MutableLiveData<Boolean>()
    val carAddedSuccess: LiveData<Boolean> = _carAddedSuccess

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData()
    val errorMessage: LiveData<String?> = _errorMessage


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

    fun addCar(brand: String, type: String, description: String = "", category: CarCategory = CarCategory.ICE, availability: Boolean = true, timeBlock: List<TimeBlock> = emptyList()) {
        val ownerId = loginViewModel.loggedInUserId.value ?: throw IllegalStateException("User is not logged in")

        val defaultLocation = Location(51.5883, 4.7750)

        val newCar = Car(
            brand = brand,
            type = type,
            description = description,
            category = category,
            availability = availability,
            timeBlock = timeBlock,
            ownerId = ownerId,
            location = defaultLocation,
            isNew = true
        )

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = CarApi.carApiService.cars(newCar)
                if (response.isSuccessful) {
                    _carAddedSuccess.value = true
                } else {
                    _errorMessage.value = "Auto toevoegen mislukt"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Er is een netwerkfout opgetreden"
            } finally {
                _isLoading.value = false
            }
        }
    }


    class CarDetailViewModelFactory(private val loginViewModel: LoginViewModel) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CarDetailViewModel::class.java)) {
                return CarDetailViewModel(loginViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}
