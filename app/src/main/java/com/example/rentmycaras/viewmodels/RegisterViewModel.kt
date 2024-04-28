package com.example.rentmycaras.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _registrationStatus = mutableStateOf(false)
    private val _errorMessage = mutableStateOf<String?>(null)

    fun register(userName: String, phone: String, email: String, password: String) {
        if (password.isNotEmpty()) { // Hier kun je een extra controle toevoegen op het wachtwoord
            viewModelScope.launch {
                // Simuleer een vertraging van 1 seconde (bijvoorbeeld voor netwerkbewerkingen)
                kotlinx.coroutines.delay(1000)

                // Voer hier verdere acties uit, bijvoorbeeld het instellen van de registratiestatus
                _registrationStatus.value = true

                // Print een bericht om aan te geven dat de registratie is voltooid
                println("Registration completed for: $userName, $phone, $email")
            }
        } else {
            // Wachtwoord is leeg, stel een foutmelding in
            _errorMessage.value = "Password cannot be empty."
        }
    }

    fun getRegistrationStatus() = _registrationStatus.value

    fun getErrorMessage(): String? {
        val errorMessage = _errorMessage.value
        _errorMessage.value = null // Reset de foutmelding nadat deze is opgehaald
        return errorMessage
    }
}
