package com.example.rentmycaras.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rentmycaras.R
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.api.CarApiService
import com.example.rentmycaras.models.Car
import com.example.rentmycaras.ui_components.FilterChipGroup
import com.example.rentmycaras.viewmodels.CarDetailViewModel
import com.example.rentmycaras.viewmodels.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val carDetailViewModel: CarDetailViewModel = viewModel(factory = CarDetailViewModel.CarDetailViewModelFactory(
        loginViewModel
    ))
    val loggedInUser by loginViewModel.loggedInUser.observeAsState()
    var showMenu by remember { mutableStateOf(false) }
    val apiCar: CarApiService = CarApi.carApiService

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

    Column(
        modifier = Modifier.padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val chipsList = listOf("Home")
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

        // Functie om auto's op te halen
        fun fetchCars() {
            showLoading = true
            scope.launch {
                when (headLine) {
                    "Home" -> {
                        val carsResponse = apiCar.getAllCars(searchInput)
                        cars.clear()
                        cars.addAll(carsResponse)
                    }
                }
                showLoading = !showLoading
            }
        }

        // Roep de functie aan om de auto's op te halen
        LaunchedEffect(key1 = null) {
            fetchCars()
        }

        Row(
            modifier = Modifier
                .align(Alignment.End)
                .clickable { showMenu = true }
        ) {
            Text(text = "${loggedInUser ?: "Onbekend"}")
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(onClick = {
                    navController.navigate("profile")
                    showMenu = false
                }) {
                    Text("Profiel")
                }
                DropdownMenuItem(onClick = {
                    navController.navigate("reservations")
                    showMenu = false
                }) {
                    Text("Reserveringen")
                }
                DropdownMenuItem(onClick = {
                    loginViewModel.logout()
                    navController.navigate("login")
                    showMenu = false
                }) {
                    Text("Afmelden")
                }
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
            label = { Text("Filter op auto of merk...") },
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

        Button(
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .width(200.dp),
            onClick = {
                showLoading = true
                scope.launch {
                    when (headLine) {
                        "Home" -> {
                            val carsResponse = apiCar.getAllCars(searchInput)
                            cars.clear()
                            cars.addAll(carsResponse)
                        }
                    }
                    showLoading = !showLoading
                }

            }) {
            Text(text = "Filter")
        }
        if (showLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        LazyVerticalGrid(columns = GridCells.Fixed(2), state = gridState, modifier = Modifier.weight(1f),
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
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onTap = {
                                    navController.navigate("carDetails/${car.id}" )
                                }
                            )
                        }
                ) {
                    Image(
                        painterResource(getCarImage(car.brand)),
                        contentDescription = "",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth()
                    )
                    Text(text = car.brand)
                    Text(text = car.type)
                    Text(text = car.owner?.name ?: "Onbekend")
                }
            }
        }

        var brand by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }

        Button(
            onClick = { showDialog = true }
        ) {
            Text(text = "Voeg auto toe")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Voeg auto toe") },
                text = {
                    Column(
                        modifier = Modifier.verticalScroll(rememberScrollState())
                    ) {
                        OutlinedTextField(
                            value = brand,
                            onValueChange = { brand = it },
                            label = { Text("Merk") }
                        )
                        OutlinedTextField(
                            value = type,
                            onValueChange = { type = it },
                            label = { Text("Type") }
                        )
                        // Voeg hier meer TextFields toe voor de andere autodetails
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        carDetailViewModel.addCar(brand, type /*, andere details */)
                        showDialog = false

                        fetchCars()
                    }) {
                        Text("Voeg auto toe")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Annuleer")
                    }
                }
            )
        }
    }
}
