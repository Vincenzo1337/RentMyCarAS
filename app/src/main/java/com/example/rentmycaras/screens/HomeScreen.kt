package com.example.rentmycaras.screens

import android.widget.GridLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rentmycaras.R
import com.example.rentmycaras.ui_components.CodeCard
import com.example.rentmycaras.ui_components.FilterChipGroup
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Car
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val carImagesMap = mapOf(
        "BMW" to R.drawable.bmw_e30,
        "Volkswagen" to R.drawable.golf_r,
        "Volvo" to R.drawable.volvo_v60
    )

    fun getCarImage(brand: String): Int {
        return carImagesMap[brand] ?: R.drawable.red_car
    }

    Column(modifier = Modifier.padding(all = 12.dp)) {

        val chipsList = listOf("/GET")
        var headLine by remember { mutableStateOf(chipsList[0]) }
        val scope = rememberCoroutineScope()

        var showLoading by remember { mutableStateOf(false) }
        var searchInput by remember {
            mutableStateOf<String?>(null)
        }
        val cars = remember {
            mutableStateListOf<Car>()
        }
        val gridState = rememberLazyGridState()


        val apiCar = CarApi.carApiService

        LaunchedEffect(key1 = null) {
            showLoading = true
            scope.launch {
                when (headLine) {
                    "/GET" -> {
                        val carsResponse = apiCar.getAllCars(searchInput)
                        cars.clear()
                        cars.addAll(carsResponse)
                    }

//                        "/GET/1" -> {
//                            jsonResponse = apiCar.getUserById(1).toString()
//                        }
                }
                showLoading = !showLoading
            }
        }

        Text(
            style = MaterialTheme.typography.headlineLarge,
            text = headLine
        )
        Divider()


        FilterChipGroup(items = chipsList,
            onSelectedChanged = { selectedIndex: Int ->
                headLine = chipsList[selectedIndex]
            })

        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
            value = searchInput ?: "",
            label = { Text("Typ hier...") },
            onValueChange = { value ->
                searchInput = value
            },
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
                            val carsResponse = apiCar.getAllCars(searchInput)
                            cars.clear()
                            cars.addAll(carsResponse)
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
        LazyVerticalGrid(columns = GridCells.Fixed(3), state = gridState, modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),) {
            items(cars.size) { index ->
                val car = cars[index]
                Card(

                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                ) {

                        Image(
                            painterResource(getCarImage(car.brand)),
                            contentDescription = "",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth()
                        )
                    Text(text = car.brand)
                    Text(text = car.type)
                    Text(text = car.owner.name ?: "Onbekend")
                }

            }
        }
    }

}
