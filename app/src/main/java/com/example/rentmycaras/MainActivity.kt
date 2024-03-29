package com.example.rentmycaras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.example.rentmycaras.screens.LoginScreen
import com.example.rentmycaras.screens.HomeScreen
import com.example.rentmycaras.screens.ProfileScreen
import com.example.rentmycaras.screens.RegisterScreen
import com.example.rentmycaras.ui.theme.RentMyCarASTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentMyCarASTheme {
//                RegisterScreen()
//                HomeScreen()
                LoginScreen()
            }
        }
    }
}
