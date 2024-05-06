package com.example.rentmycaras.viewmodels

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Account
import kotlinx.coroutines.launch


class ProfileViewModel(private val loginViewModel: LoginViewModel) : ViewModel() {

    fun updateProfile(phone: TextFieldValue, password: String) {
        val userId = loginViewModel.loggedInUserId.value
        val userName = loginViewModel.loggedInUser.value
        val email = loginViewModel.loggedInEmail.value
        if (userId != null && userName != null && email != null) {
            // Maak een nieuw Account object met de bijgewerkte telefoon en wachtwoord
            val updatedAccount = Account(
                userName = userName,
                password = password,
                userId = userId,
                phone = phone.text,
                email = email
            )

            viewModelScope.launch {
                val response = CarApi.carApiService.updateAccount(userName, updatedAccount)
                println("Response: $response") // Dit drukt de volledige response af
                if (response.isSuccessful) {
                    println("Updated profile: $updatedAccount")
                } else {
                    println("Failed to update profile")
                    println("Status code: ${response.code()}")
                    println("Error body: ${response.errorBody()?.string()}")
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
