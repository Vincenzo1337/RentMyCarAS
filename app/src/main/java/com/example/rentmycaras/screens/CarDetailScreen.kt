package com.example.rentmycaras.screens

import Reservation
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rentmycaras.R
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.TimeBlock
import com.example.rentmycaras.viewmodels.CarDetailViewModel
import com.example.rentmycaras.viewmodels.LoginViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun CarDetailScreen(
    navController: NavController,
    carDetailViewModel: CarDetailViewModel,
    loginViewModel: LoginViewModel
) {

    val carImagesMap = mapOf(
        "BMW" to R.drawable.bmw_e30,
        "Volkswagen" to R.drawable.golf_r,
        "Volvo" to R.drawable.volvo_v60
    )

    val available = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = null) {
        CarApi.carApiService.getAvailability(carDetailViewModel.car.value?.id ?: 0).let {
            available.value = it.body() ?: false
        }
    }

    fun getCarImage(brand: String): Int {
        return carImagesMap[brand] ?: R.drawable.red_car
    }

    val car = remember { carDetailViewModel.car }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LaunchedEffect(key1 = null) {
            carDetailViewModel.getCar()
        }

        IconButton(onClick = { navController.navigate("home") }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Ga terug naar Home")
        }

        Image(
            painter = painterResource(id = getCarImage(car.value?.brand ?: "")),
            contentDescription = null,
            modifier = Modifier
                .size(width = 350.dp, height = 200.dp)
                .clip(RoundedCornerShape(4.dp))
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Merk: ",
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
            text = "Beschikbaar: ",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = car.value?.availability?.let { if (it) "Ja" else "Nee" } ?: "")
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Beschrijving: ",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(text = car.value?.description ?: "")

        Button(onClick = {
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
            }
        }, enabled = car.value?.availability == true && available.value) {
            Text(text = "Reserveer")
        }
    }
}