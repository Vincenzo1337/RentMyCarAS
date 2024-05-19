package com.example.rentmycaras.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ReservationScreen(navController: NavController) {
    Column(modifier = Modifier.padding(16.dp)) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Ga terug naar de home pagina")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Mijn reserveringen",
            style = MaterialTheme.typography.h5)
        // Voeg hier de rest van je UI toe
    }
}
