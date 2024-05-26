package com.example.rentmycaras.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rentmycaras.R
import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.api.CarApiService
import com.example.rentmycaras.models.Car
import com.example.rentmycaras.models.CarCategory
import com.example.rentmycaras.ui_components.FilterChipGroup
import com.example.rentmycaras.viewmodels.CarDetailViewModel
import com.example.rentmycaras.viewmodels.LoginViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, loginViewModel: LoginViewModel = viewModel()) {
    val carDetailViewModel: CarDetailViewModel = viewModel(
        factory = CarDetailViewModel.CarDetailViewModelFactory(
            loginViewModel
        )
    )
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Home", "Contact")
    androidx.compose.material3.Scaffold(
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
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            androidx.compose.material3.DropdownMenuItem(
                                onClick = {
                                    navController.navigate("profile")
                                    showMenu = false
                                },
                                text = {
                                    Text(
                                        text = "profiel",
//                                        text = stringResource(id = R.string.profiel),
                                        color = Color.Black // Stel de kleur van de tekst hier in
                                    )
                                }
                            )

                            androidx.compose.material3.DropdownMenuItem(
                                onClick = {
                                    loginViewModel.logout()
                                    navController.navigate("login")
                                    showMenu = false
                                },
                                text = {
                                    Text(
                                        text = "afmelden",
//                                        text = stringResource(id = R.string.afmelden),
                                        color = Color.Black // Stel de kleur van de tekst hier in
                                    )
                                }
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
fun HomeContent(navController: NavController, loginViewModel: LoginViewModel = viewModel(), carDetailViewModel: CarDetailViewModel = viewModel()){
    val carImagesMap = mapOf(
        "BMW" to R.drawable.bmw_e30,
        "Volkswagen" to R.drawable.golf_r,
        "Volvo" to R.drawable.volvo_v60,
        "Toyota" to R.drawable.toyota_corolla,
        "Ford" to R.drawable.ford_mustang,
        "Honda" to R.drawable.honda_civic
    )
    val loggedInUser by loginViewModel.loggedInUser.observeAsState()
    var showMenu by remember { mutableStateOf(false) }
    val apiCar: CarApiService = CarApi.carApiService

    fun getCarImage(brand: String): Int {
        return carImagesMap[brand] ?: R.drawable.red_car
    }

    Column(
        modifier = Modifier.padding(all = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val chipsList = listOf("")
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
                    "" -> {
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


        Text(
            style = MaterialTheme.typography.headlineLarge,
            text = headLine
        )
//        Divider()

//        FilterChipGroup(items = chipsList,
//            onSelectedChanged = { selectedIndex: Int ->
//                headLine = chipsList[selectedIndex]
//            })

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
                .width(200.dp)
                .background(
                    color = Color.Blue,
                    shape = RoundedCornerShape(percent = 50)
                ),
        onClick = {
                showLoading = true
                scope.launch {
                    when (headLine) {
                        "" -> {
                            val carsResponse = apiCar.getAllCars(searchInput)
                            cars.clear()
                            cars.addAll(carsResponse)
                        }
                    }
                    showLoading = !showLoading
                }
            },
                colors = ButtonDefaults.buttonColors(
                containerColor = Color.Blue,
                contentColor = Color.White
        )
        )  {
            Text(text = "Filter")
        }
        if (showLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), state = gridState, modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
        ) {
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
                                    navController.navigate("carDetails/${car.id}")
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
                    Text(text = car.category.name)
                    Text(text = car.owner?.name ?: "Onbekend")
                }
            }
        }

        var brand by remember { mutableStateOf("") }
        var type by remember { mutableStateOf("") }
        var carCategory by remember { mutableStateOf(CarCategory.ICE) }
        var description by remember { mutableStateOf("") }
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
                        OutlinedTextField(
                            value = carCategory.name,
                            onValueChange = { newCategory ->
                                try {
                                    carCategory =
                                        CarCategory.valueOf(newCategory.toUpperCase())
                                } catch (e: IllegalArgumentException) {
                                    // Toon een foutmelding aan de gebruiker
                                }
                            },
                            label = { Text("Brandstoftype") }
                        )
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Beschrijving") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        carDetailViewModel.addCar(
                            brand,
                            type,
                            description,
                            carCategory /*, andere details */
                        )
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
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactContent() {
    var uiSettings by remember { mutableStateOf(MapUiSettings()) }
    val properties by remember { mutableStateOf(MapProperties(mapType = MapType.SATELLITE)) }
    val avansHA = LatLng(51.58466, 4.797556)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(avansHA, 18f, 45f, 270f)

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
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Tekstbox
        Text(
            text = "Wij zijn bereikbaar op deze locatie:",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(), // Voeg deze modifier toe
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        if (permissionsState.allPermissionsGranted) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = properties,
                uiSettings = uiSettings
            ) {
                Marker(
                    state = MarkerState(position = avansHA),
                    title = "Avans Hogeschool",
                    snippet = "Hogeschoollaan 1, Breda"
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
}