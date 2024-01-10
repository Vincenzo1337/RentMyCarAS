package com.example.rentmycaras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _loginStatus = mutableStateOf(false)
    private val _errorMessage = mutableStateOf<String?>(null)

    fun login(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            viewModelScope.launch {
                // Simuleer een vertraging van 1 seconde (bijvoorbeeld voor netwerkbewerkingen)
                kotlinx.coroutines.delay(1000)

                // Voer hier verdere acties uit, bijvoorbeeld het instellen van de inlogstatus
                _loginStatus.value = true

                // Print een bericht om aan te geven dat de inlogpoging is geslaagd
                println("Login successful for: $username")
            }
        } else {
            // Gebruikersnaam of wachtwoord is leeg, stel een foutmelding in
            _errorMessage.value = "Please enter both username and password."
        }
    }

    fun getLoginStatus() = _loginStatus.value

    fun getErrorMessage(): String? {
        val errorMessage = _errorMessage.value
        _errorMessage.value = null // Reset de foutmelding nadat deze is opgehaald
        return errorMessage
    }
}
