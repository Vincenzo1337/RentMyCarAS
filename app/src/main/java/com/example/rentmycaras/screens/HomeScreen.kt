package com.example.rentmycaras.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentmycaras.ui_components.CodeCard
import com.example.rentmycaras.ui_components.FilterChipGroup
import com.example.rentmycaras.api.CarApi
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    Column(modifier = Modifier.padding(all = 12.dp)) {

        val chipsList = listOf("/GET")
        var headLine by remember { mutableStateOf(chipsList[0]) }
        val scope = rememberCoroutineScope()
        var jsonResponse by remember { mutableStateOf("") }
        var showLoading by remember { mutableStateOf(false) }

        val apiCar = CarApi.carApiService

        Text(
            style = MaterialTheme.typography.headlineLarge,
            text = headLine
        )
        Divider()


        FilterChipGroup(items = chipsList,
            onSelectedChanged = { selectedIndex: Int ->
                headLine = chipsList[selectedIndex]
                jsonResponse = ""
            })

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = "Typ hier...",
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Filled.Lock,
                    contentDescription = "Localized Description",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(modifier = Modifier
            .align(alignment = Alignment.CenterHorizontally)
            .width(200.dp),
            onClick = {
                showLoading = true
                scope.launch {
                    when (headLine) {
                        "/GET" -> {
                            jsonResponse = apiCar.getAllCars().toString()
                        }

//                        "/GET/1" -> {
//                            jsonResponse = apiCar.getUserById(1).toString()
//                        }
                    }
                    showLoading = !showLoading
                }

            }) {
            Text(text = "Send")
        }
        if (showLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        CodeCard(jsonStr = jsonResponse)
    }

}
