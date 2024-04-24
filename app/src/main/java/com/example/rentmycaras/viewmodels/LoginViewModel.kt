package com.example.rentmycaras.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Account
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private var _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private var _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val response = CarApi.carApiService.login(Account(username, password, 0, "", "", ""))
                if (response.isSuccessful) {
                    _loginSuccess.value = true

                } else {
                    _errorMessage.value = "Inloggen mislukt"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Er is een netwerkfout opgetreden"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}
