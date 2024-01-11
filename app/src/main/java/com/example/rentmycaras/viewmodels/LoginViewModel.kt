package com.example.rentmycaras.viewmodels

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginStatus = mutableStateOf(false)
    private val _errorMessage = mutableStateOf<String?>(null)
    private val _isLoading = mutableStateOf(false)

    val isLoading: State<Boolean> = _isLoading

    fun login(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            _isLoading.value = true
            viewModelScope.launch {
                try {
                    kotlinx.coroutines.delay(1000)
                    _loginStatus.value = true
                    println("Login successful for: $username")
                } catch (e: Exception) {
                    setErrorMessage("Login failed. Please check your credentials.")
                } finally {
                    _isLoading.value = false
                }
            }
        } else {
            setErrorMessage("Please enter both username and password.")
        }
    }

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

    fun getLoginStatus() = _loginStatus.value

    fun getErrorMessage(): String? {
        val errorMessage = _errorMessage.value
        _errorMessage.value = null // Reset de foutmelding nadat deze is opgehaald
        return errorMessage
    }
}

