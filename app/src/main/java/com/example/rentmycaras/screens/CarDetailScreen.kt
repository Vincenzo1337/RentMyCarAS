package com.example.rentmycaras.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rentmycaras.viewmodels.CarDetailViewModel

@Composable
fun CarDetailScreen(carDetailViewModel: CarDetailViewModel = viewModel()) {

    val car = remember { carDetailViewModel.car }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LaunchedEffect(key1 = null) {
            carDetailViewModel.getCar()
        }

        Text(text = car.value?.brand ?: "")
    }
}