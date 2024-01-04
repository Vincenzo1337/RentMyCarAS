package com.example.rentmycaras.screens

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rentmycaras.ui.theme.RentMyCarASTheme
import com.example.rentmycaras.viewmodels.RegisterViewModel

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = viewModel()) {

    var name by remember { mutableStateOf(TextFieldValue()) }
    var phone by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var passwordValue by remember { mutableStateOf(TextFieldValue()) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Register",
            style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.CenterHorizontally)
        )

        TextField(
            value = name.text,
            onValueChange = {
                name = it.text
            },
            label = { Text("Name") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = phone.text,
            onValueChange = {
                phone = it.text
            },
            label = { Text("Phonenumber") },
            leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = email.text,
            onValueChange = {
                email = it.text
            },
            label = { Text("E-mail") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TextField(
            value = passwordValue.text,
            onValueChange = {
                passwordValue = it.text
            },
            label = { Text("Password") },
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
            label = { Text("Confirm Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (passwordValue.text == confirmPassword.text) {
                    registerViewModel.register(
                        name = name.text,
                        phone = phone.text,
                        email = email.text,
                        password = passwordValue.text
                    )
                } else {
                    // Toon een foutmelding dat wachtwoorden niet overeenkomen
                    // Je kunt ook een andere manier kiezen om gebruikers te informeren over onjuiste wachtwoorden
                    // bijv. een toast of een foutmelding in de UI.
                    println("Passwords do not match.")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Register")
        }


    }
}

@Composable
fun RegisterScreenPreview() {
    RentMyCarASTheme {
        RegisterScreen()
    }
}
