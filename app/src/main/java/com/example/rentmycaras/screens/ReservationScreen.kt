package com.example.rentmycaras.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rentmycaras.R
import com.example.rentmycaras.viewmodels.LoginViewModel
import com.example.rentmycaras.viewmodels.ReservationViewModel

@Composable
fun ReservationScreen(navController: NavController, loginViewModel: LoginViewModel) {
    val factory = ReservationViewModel.ReservationViewModelFactory(loginViewModel)
    val reservationViewModel: ReservationViewModel = viewModel(factory = factory)

    val reservations by reservationViewModel.reservations.observeAsState(emptyList())
    val cars by reservationViewModel.cars.observeAsState(emptyList())

    val carImagesMap = mapOf(
        "BMW" to R.drawable.bmw_e30,
        "Volkswagen" to R.drawable.golf_r,
        "Volvo" to R.drawable.volvo_v60,
        "Toyota" to R.drawable.toyota_corolla,
        "Ford" to R.drawable.ford_mustang,
        "Honda" to R.drawable.honda_civic
    )

    fun getCarImage(brand: String): Int {
        return carImagesMap[brand] ?: R.drawable.red_car
    }

    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Ga terug naar de home pagina")
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = stringResource(id = R.string.reserveringen),
            style = MaterialTheme.typography.h5)

        LazyColumn {
            items(reservations.zip(cars)) { (reservation, car) ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painterResource(getCarImage(car.brand)),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                    )
                    Text(text = "Merk: ${car.brand}")
                    Text(text = "Type: ${car.type}")
                }
            }
        }
    }
}



