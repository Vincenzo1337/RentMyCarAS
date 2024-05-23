package com.example.rentmycaras.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.rentmycaras.models.Car
import com.example.rentmycaras.viewmodels.LoginViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Home", "Contact")

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(text = "Rent My Car", color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Blue
                    ),
                    actions = {
                        var showMenu by remember { mutableStateOf(false) }
                        val loggedInUser by loginViewModel.loggedInUser.observeAsState()

                        Row(
                            modifier = Modifier
                                .clickable { showMenu = true }
                                .padding(end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "${loggedInUser ?: "Onbekend"}", color = Color.White)
                            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                onClick = {
                                    navController.navigate("profile")
                                    showMenu = false
                                },
                                text = { Text("Profiel") }
                            )
                            DropdownMenuItem(
                                onClick = {
                                    loginViewModel.logout()
                                    navController.navigate("login")
                                    showMenu = false
                                },
                                text = { Text("Afmelden") }
                            )
                        }
                    }
                )
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = title, modifier = Modifier.padding(16.dp)) }
                        )
                    }
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(all = 12.dp)
                    .background(Color(0xFFE0E0E0)) // Light grey background
            ) {
                when (selectedTabIndex) {
                    0 -> HomeContent(navController, loginViewModel)
                    1 -> ContactContent()
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(navController: NavController, loginViewModel: LoginViewModel) {
    val carImagesMap = mapOf(
        "BMW" to R.drawable.bmw_e30,
        "Volkswagen" to R.drawable.golf_r,
        "Volvo" to R.drawable.volvo_v60
    )

    fun getCarImage(brand: String): Int {
        return carImagesMap[brand] ?: R.drawable.red_car
    }

    val scope = rememberCoroutineScope()
    var showLoading by remember { mutableStateOf(false) }
    var searchInput by remember { mutableStateOf<String?>(null) }
    val cars = remember { mutableStateListOf<Car>() }
    val gridState = rememberLazyGridState()

    val apiCar = CarApi.carApiService

    LaunchedEffect(key1 = null) {
        showLoading = true
        scope.launch {
            val carsResponse = apiCar.getAllCars(searchInput)
            cars.clear()
            cars.addAll(carsResponse)
            showLoading = false
        }
    }

    Text(
        style = MaterialTheme.typography.headlineLarge,
        text = "Home",
        color = Color.DarkGray // Dark gray headline color
    )
    Divider(color = Color.Black) // Black divider color

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
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
            .width(200.dp),
        onClick = {
            showLoading = true
            scope.launch {
                val carsResponse = apiCar.getAllCars(searchInput)
                cars.clear()
                cars.addAll(carsResponse)
                showLoading = false
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Blue,
            contentColor = Color.White
        ) // Blue button with white text
    ) {
        Text(text = "Filter")
    }

    if (showLoading) {
        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color.Blue) // Blue progress indicator
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 16.dp,
            end = 12.dp,
            bottom = 16.dp
        )
    ) {
        items(cars) { car ->
            CarCard(car, navController, carImagesMap)
        }
    }
}

@Composable
fun CarCard(car: Car, navController: NavController, carImagesMap: Map<String, Int>) {
    fun getCarImage(brand: String): Int {
        return carImagesMap[brand] ?: R.drawable.red_car
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        navController.navigate("carDetails/${car.id}")
                    }
                )
            }
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .background(Color.White) // White card background
        ) {
            Image(
                painter = painterResource(getCarImage(car.brand)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
            )
            Text(text = car.brand, color = Color.Black) // Black text color
            Text(text = car.type, color = Color.DarkGray) // Dark gray text color
            Text(text = car.owner.name ?: "Onbekend", color = Color.DarkGray) // Dark gray text color
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactContent() {
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(LatLng(37.7749, -122.4194), 12f)
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(Unit) {
        permissionsState.launchMultiplePermissionRequest()
    }

    if (permissionsState.allPermissionsGranted) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            Marker(
//                position = LatLng(37.7749, -122.4194),
                title = "San Francisco",
                snippet = "Marker in San Francisco"
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Location permissions are required to display the map.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                Text("Grant Permissions")
            }
        }
    }
}
