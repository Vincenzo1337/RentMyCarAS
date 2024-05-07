package com.example.rentmycaras.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rentmycaras.viewmodels.LoginViewModel
import com.example.rentmycaras.viewmodels.ProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


val String.text: TextFieldValue
    get() = TextFieldValue(this)

@Composable
fun ProfileScreen(navController: NavController, loginViewModel: LoginViewModel, profileViewModel: ProfileViewModel) {
    val username = loginViewModel.loggedInUser.value ?: ""
    val email = loginViewModel.loggedInEmail.value ?: "" // Haal het e-mailadres op
    val validationError by profileViewModel.validationError.collectAsState()
    val updateSuccess by profileViewModel.updateSuccess.collectAsState()

    var phone by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(key1 = profileViewModel) {
        val currentPhone = profileViewModel.getCurrentPhoneNumber()
        phone = TextFieldValue(currentPhone)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { navController.navigate("home") }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Ga terug naar de home pagina")
        }

        Text(
            text = "Profiel",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        TextField(
            value = username,
            onValueChange = {},
            label = { Text("Gebruikersnaam") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = false
        )

        TextField(
            value = email,
            onValueChange = {},
            label = { Text("E-mail") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            enabled = false
        )

        TextField(
            value = phone.text,
            onValueChange = {
                phone = it.text
            },
            label = { Text("Telefoonnummer") },
            leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = password.text,
            onValueChange = {
                password = it.text
            },
            label = { Text("Wachtwoord") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = confirmPassword.text,
            onValueChange = {
                confirmPassword = it.text
            },
            label = { Text("Bevestig wachtwoord") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (profileViewModel.validateInput(
                        phone.text,
                        password.text,
                        confirmPassword.text
                    )) {
                    CoroutineScope(Dispatchers.IO).launch {
                        profileViewModel.updateProfile(
                            phone,
                            password.text
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Update profiel")
        }

        if (validationError != null) {
            AlertDialog(
                onDismissRequest = { profileViewModel.clearValidationError() },
                title = { Text("Validatiefout") },
                text = { Text(validationError!!) },
                confirmButton = {
                    Button(onClick = { profileViewModel.clearValidationError() }) {
                        Text("OK")
                    }
                }
            )
        }

        if (updateSuccess == true) {
            AlertDialog(
                onDismissRequest = { profileViewModel.clearUpdateSuccess() },
                title = { Text("Profiel bijgewerkt") },
                text = { Text("Wijzigingen opgeslagen.") },
                confirmButton = {
                    Button(onClick = {
                        profileViewModel.clearUpdateSuccess()
                        navController.navigate("home")
                    }) {
                        Text("OK")
                    }
                })
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(loginViewModel))

    RentMyCarASTheme {
        ProfileScreen(navController, loginViewModel, profileViewModel)
    }
}*/


