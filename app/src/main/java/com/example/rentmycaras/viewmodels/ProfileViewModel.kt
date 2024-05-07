package com.example.rentmycaras.viewmodels

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class ProfileViewModel(private val loginViewModel: LoginViewModel) : ViewModel() {

    private val _updateSuccess = MutableStateFlow<Boolean?>(null)
    val updateSuccess: StateFlow<Boolean?> = _updateSuccess

    suspend fun getCurrentPhoneNumber(): String {
        val userName = loginViewModel.loggedInUser.value
        if (userName != null) {
            val response = CarApi.carApiService.getAccountByUsername(userName)
            if (response.isSuccessful) {
                val account = response.body()
                return account?.phone ?: ""
            }
        }
        return ""
    }

    fun clearUpdateSuccess() {
        _updateSuccess.value = null
    }

    fun isValidPhoneNumber(phone: String): Boolean {
        return phone.all { it.isDigit() || it == '+' }
    }

    fun clearValidationError() {
        _validationError.value = null
    }

    private val _validationError = MutableStateFlow<String?>(null)
    val validationError: StateFlow<String?> = _validationError

    fun validateInput(phone: String, password: String, confirmPassword: String): Boolean {
        var errorMessage: String? = null

        if (phone.isNotEmpty()) {
            errorMessage = when {
                !isValidPhoneNumber(phone) -> "Telefoonnummer mag alleen cijfers en het + teken bevatten."
                phone.length > 15 -> "Telefoonnummer mag maximaal 15 karakters zijn."
                else -> null
            }
        }

        if (password.isNotEmpty() || confirmPassword.isNotEmpty()) {
            errorMessage = when {
                password.isEmpty() || confirmPassword.isEmpty() -> "Wachtwoordvelden mogen niet leeg zijn."
                password != confirmPassword -> "Wachtwoorden komen niet overeen."
                password.length > 50 -> "Wachtwoord mag maximaal 50 karakters zijn."
                else -> null
            }
        }

        _validationError.value = errorMessage
        return errorMessage == null
    }

    suspend fun updateProfile(phone: TextFieldValue, password: String) {
        val userId = loginViewModel.loggedInUserId.value
        val userName = loginViewModel.loggedInUser.value
        val email = loginViewModel.loggedInEmail.value
        if (userId != null && userName != null && email != null) {
            // Haal de huidige waarden van het telefoonnummer en het wachtwoord op
            val currentAccountResponse = CarApi.carApiService.getAccountByUsername(userName)
            if (currentAccountResponse.isSuccessful) {
                val currentAccount = currentAccountResponse.body()
                val currentPhone = currentAccount?.phone ?: ""
                val currentPassword = currentAccount?.password ?: ""

                // Gebruik de nieuwe waarde als deze is ingevoerd, anders gebruik je de oude waarde
                val updatedPhone = if (phone.text.isNotEmpty()) phone.text else currentPhone
                val updatedPassword = if (password.isNotEmpty()) password else currentPassword

                val updatedAccount = Account(
                    userName = userName,
                    password = updatedPassword,
                    userId = userId,
                    phone = updatedPhone,
                    email = email
                )

                val response = CarApi.carApiService.updateAccount(userName, updatedAccount)
                if (response.isSuccessful) {
                    _updateSuccess.value = true
                    println("Updated profile: $updatedAccount")
                } else {
                    _updateSuccess.value = false
                    println("Failed to update profile")
                }
            }
        }
    }
}

    class ProfileViewModelFactory(private val loginViewModel: LoginViewModel) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(loginViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
