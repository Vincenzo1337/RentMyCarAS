package com.example.rentmycaras.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rentmycaras.ui.theme.RentMyCarASTheme

private val String.text: TextFieldValue
    get() = TextFieldValue(this)


@Composable
fun ProfileScreen() {
    var name by remember { mutableStateOf(TextFieldValue("John Doe")) }
    var phone by remember { mutableStateOf(TextFieldValue("+123456789")) }
    var email by remember { mutableStateOf(TextFieldValue("john.doe@example.com")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Profiel",
            style = TextStyle(
                fontSize = 30.sp, // Pas de grootte naar wens aan
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TextField(
            value = name.text,
            onValueChange = {
                name = it.text
            },
            label = { Text("Naam") },
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
            label = { Text("Telefoonnummer") },
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
            label = { Text("E-mailadres") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                // Voeg hier code toe om het profiel bij te werken
                // Je kunt gebruik maken van de ingevoerde gegevens: name, phone, email, password
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Profiel Bijwerken")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    RentMyCarASTheme {
        ProfileScreen()
    }
}
