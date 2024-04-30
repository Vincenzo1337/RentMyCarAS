package com.example.rentmycaras.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel : ViewModel() {
    private val _registrationStatus = MutableStateFlow(false)
    private val _errorMessage = MutableStateFlow<String?>(null)

    val registrationStatus: StateFlow<Boolean> = _registrationStatus
    val errorMessage: StateFlow<String?> = _errorMessage

    suspend fun register(userName: String, phone: String, email: String, password: String): Boolean {
        var registrationSuccessful = false
        if (password.isNotEmpty()) { // Hier kun je een extra controle toevoegen op het wachtwoord
            // Simuleer een vertraging van 1 seconde (bijvoorbeeld voor netwerkbewerkingen)
            kotlinx.coroutines.delay(1000)

            // Roep de register functie van de API aan
            val response = CarApi.carApiService.register(Account(userName, password, 0, phone, email))
            if (response.isSuccessful) {
                _registrationStatus.value = true
                _errorMessage.value = "Registratie succesvol!"
                println("Registration successful")
                registrationSuccessful = true
            } else {
                _registrationStatus.value = false
                _errorMessage.value = "Registratie mislukt. Probeer het opnieuw."
                println("Registration failed")
            }
        } else {
            // Wachtwoord is leeg, stel een foutmelding in
            _errorMessage.value = "Wachtwoord mag niet leeg zijn."
        }
        return registrationSuccessful
    }


    fun getRegistrationStatus() = _registrationStatus.value

    fun getErrorMessage(): String? {
        val errorMessage = _errorMessage.value
        _errorMessage.value = null // Reset de foutmelding nadat deze is opgehaald
        return errorMessage
    }
}
