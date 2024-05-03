package com.example.rentmycaras.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentmycaras.screens.UserProfile
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _userProfile = mutableStateOf(UserProfile("", "", "", ""))
    fun updateProfile(phone: String, password: String) {
        // Haal de huidige gebruikersprofiel op
        val currentProfile = _userProfile.value

        // Maak een nieuw profiel met de bijgewerkte telefoon en wachtwoord
        val newProfile = UserProfile(
            currentProfile.username, // behoud de huidige gebruikersnaam
            phone, // update telefoonnummer
            currentProfile.email, // behoud de huidige e-mail
            password // update wachtwoord
        )

        _userProfile.value = newProfile

        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            println("Updated profile: $newProfile")
        }
    }
}
