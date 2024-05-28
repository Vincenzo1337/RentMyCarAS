package com.example.rentmycaras.screens

import Reservation
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.rentmycaras.R
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.TimeBlock
import com.example.rentmycaras.viewmodels.CarDetailViewModel
import com.example.rentmycaras.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate


@Composable
fun CarDetailScreen(
    navController: NavController,
    carDetailViewModel: CarDetailViewModel,
    loginViewModel: LoginViewModel,
    backStackEntry: NavBackStackEntry
) {
    val carImagesMap = mapOf(
        "BMW" to R.drawable.bmw_e30,
        "Volkswagen" to R.drawable.golf_r,
        "Volvo" to R.drawable.volvo_v60,
        "Toyota" to R.drawable.toyota_corolla,
        "Ford" to R.drawable.ford_mustang,
        "Honda" to R.drawable.honda_civic
    )

    val available = remember {
        mutableStateOf(true)
    }

    val carId = backStackEntry.arguments?.getString("carId")
    LaunchedEffect(carId) {
        carId?.let {
            carDetailViewModel.getCar(it)
            //TODO move this to carDetailViewModel
            CarApi.carApiService.getAvailability(it.toInt()).let {
                available.value = it.body() ?: false
            }
        }
    }

    fun getCarImage(brand: String): Int {
        return carImagesMap[brand] ?: R.drawable.red_car
    }

    val car = remember { carDetailViewModel.car }
    val reservationSuccess by carDetailViewModel.reservationSuccess.observeAsState()

    LaunchedEffect(reservationSuccess) {
        if (reservationSuccess == true) {
            navController.navigate("home")
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            IconButton(onClick = { navController.navigate("home") }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Ga terug naar Home")
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = getCarImage(car.value?.brand ?: "")),
                    contentDescription = null,
                    modifier = Modifier
                        .size(width = 350.dp, height = 200.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .align(Alignment.Center),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.merk),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = car.value?.brand ?: "")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Type: ",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = car.value?.type ?: "")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.brandstof),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = car.value?.category?.name ?: "")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.beschikbaar),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = car.value?.availability?.let { if (it) "Ja" else "Nee" } ?: "")
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.beschrijving),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(text = car.value?.description ?: "")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        val response = CarApi.carApiService.createReservation(
                            Reservation(
                                userid = loginViewModel.loggedInUserId.value ?: 0,
                                carId = car.value?.id ?: 0,
                                timeBlock = TimeBlock(
                                    LocalDate.now().toEpochDay(),
                                    LocalDate.now().toEpochDay() + 1
                                ),
                                price = 10
                            )
                        )
                        withContext(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                val message = response.body()?.string()
                                if (message == "Reservering toegevoegd!") {
                                    carDetailViewModel.updateReservationSuccess(true)
                                } else {
                                    carDetailViewModel.updateReservationSuccess(false)
                                }
                            } else {
                                carDetailViewModel.updateReservationSuccess(false)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                enabled = car.value?.availability == true && available.value
            ) {
                Text(text = stringResource(id = R.string.reserveren_auto))
            }

            if (carDetailViewModel.reservationSuccess.value == true) {
                AlertDialog(
                    onDismissRequest = { carDetailViewModel.clearReservationSuccess() },
                    title = { Text("Reservering") },
                    text = { Text("Reservering succesvol gemaakt!") },
                    confirmButton = {
                        Button(onClick = {
                            carDetailViewModel.clearReservationSuccess()
                            navController.navigate("home")
                        }) {
                            Text("OK")
                        }
                    }
                )
            }

            if (carDetailViewModel.reservationSuccess.value == false) {
                AlertDialog(
                    onDismissRequest = { carDetailViewModel.clearReservationSuccess() },
                    title = { Text("Reservering") },
                    text = { Text("Er is een fout opgetreden bij het maken van de reservering.") },
                    confirmButton = {
                        Button(onClick = {
                            carDetailViewModel.clearReservationSuccess()
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}


