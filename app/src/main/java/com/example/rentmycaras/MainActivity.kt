package com.example.rentmycaras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rentmycaras.screens.CarDetailScreen
import com.example.rentmycaras.screens.HomeScreen
import com.example.rentmycaras.screens.LoginScreen
import com.example.rentmycaras.screens.ProfileScreen
import com.example.rentmycaras.screens.RegisterScreen
import com.example.rentmycaras.screens.ReservationScreen
import com.example.rentmycaras.ui.theme.RentMyCarASTheme
import com.example.rentmycaras.viewmodels.CarDetailViewModel
import com.example.rentmycaras.viewmodels.LoginViewModel
import com.example.rentmycaras.viewmodels.ProfileViewModel
import com.example.rentmycaras.viewmodels.ProfileViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RentMyCarASTheme {
                ParentComposable()
            }
        }
    }
}

@Composable
fun ParentComposable() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()

    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController, loginViewModel) }
        composable("home") { HomeScreen(navController, loginViewModel) }
        composable("register") { RegisterScreen(navController) }
        composable("reservations") { ReservationScreen(navController, loginViewModel) }
        composable("profile") { backStackEntry ->
            val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(loginViewModel))
            ProfileScreen(navController, loginViewModel, profileViewModel)
        }
        composable("carDetails/{carId}") { backStackEntry ->
            val carDetailViewModel: CarDetailViewModel = viewModel(
                factory = CarDetailViewModel.CarDetailViewModelFactory(
                    loginViewModel,
                    backStackEntry.savedStateHandle
                )
            )
            CarDetailScreen(navController, carDetailViewModel, loginViewModel, backStackEntry)
        }
    }
}


