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

    fun validateInput(
        password: String,
        confirmPassword: String,
        name: String,
        phone: String,
        email: String
    ): String? {
        var emptyFields = 0
        if (name.isEmpty()) emptyFields++
        if (phone.isEmpty()) emptyFields++
        if (email.isEmpty()) emptyFields++

        if (emptyFields >= 2) {
            return "Vul alle velden"
        }

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            return "Wachtwoordvelden mogen niet leeg zijn."
        }

        return when {
            password.isEmpty() || confirmPassword.isEmpty() -> "Wachtwoordvelden mogen niet leeg zijn."
            password != confirmPassword -> "Wachtwoorden komen niet overeen."
            phone.isEmpty() -> "Telefoonnummer is verplicht."
            !isValidPhoneNumber(phone) -> "Telefoonnummer mag alleen cijfers en het + teken bevatten."
            name.isEmpty() -> "Gebruikersnaam is verplicht."
            name.length > 20 -> "Gebruikersnaam mag maximaal 20 karakters zijn."
            phone.length > 15 -> "Telefoonnummer mag maximaal 15 karakters zijn."
            password.length > 50 -> "Wachtwoord mag maximaal 50 karakters zijn."
            email.length > 30 || !isValidEmail(email) -> "E-mailadres moet een geldig formaat hebben en mag maximaal 30 karakters zijn."
            else -> null
        }
    }




    suspend fun register(userName: String, phone: String, email: String, password: String): Boolean {
        var registrationSuccessful = false
        if (password.isNotEmpty()) {
            kotlinx.coroutines.delay(1000)
            val response = CarApi.carApiService.register(Account(userName, password, 0, phone, email))
            if (response.isSuccessful) {
                _registrationStatus.value = true
                registrationSuccessful = true
            } else {
                _registrationStatus.value = false
                println("Registration failed")
            }
        } else {
            _errorMessage.value = "Wachtwoord mag niet leeg zijn."
        }
        return registrationSuccessful
    }


    fun getRegistrationStatus() = _registrationStatus.value

    fun getErrorMessage(): String? {
        val errorMessage = _errorMessage.value
        _errorMessage.value = null
        return errorMessage
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        return phone.all { it.isDigit() || it == '+' }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}
