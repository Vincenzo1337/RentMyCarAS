package com.example.rentmycaras.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.screens.UserProfile
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _userProfile = mutableStateOf(UserProfile("", "", "", ""))
    fun updateProfile(name: String, phone: String, email: String, password: String) {
        val newProfile = UserProfile(name, phone, email, password)
        _userProfile.value = newProfile

        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            println("Updated profile: $newProfile")
        }
    }
}
