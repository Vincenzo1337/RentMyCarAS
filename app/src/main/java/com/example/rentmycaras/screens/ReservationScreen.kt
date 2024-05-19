package com.example.rentmycaras.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rentmycaras.viewmodels.LoginViewModel
import com.example.rentmycaras.viewmodels.ReservationViewModel

@Composable
fun ReservationScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val factory = ReservationViewModel.ReservationViewModelFactory(loginViewModel)
    val reservationViewModel: ReservationViewModel = viewModel(factory = factory)


    val reservations by reservationViewModel.reservations.observeAsState(emptyList()) // Observeer de reserveringen LiveData

    // Verwijder de LaunchedEffect en CoroutineScope blokken, omdat de fetchReservations() functie nu wordt aangeroepen in de ReservationViewModel

    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Ga terug naar de home pagina")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Mijn reserveringen",
            style = MaterialTheme.typography.h5)

        LazyColumn {
            items(reservations) { reservation ->
                Text(text = "Reservering: ${reservation.carId}")
            }
        }
    }
}


