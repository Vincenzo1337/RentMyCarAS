package com.example.rentmycaras

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rentmycaras.screens.LoginScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ProfileScreenTest.TestActivity>()

    @Test
    fun testLoginScreen() {
        composeTestRule.setContent {
            LoginScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Gebruikersnaam")
            .performTextInput("testUsername")

        composeTestRule.onNodeWithText("Wachtwoord")
            .performTextInput("testPassword")

        composeTestRule.onNodeWithText("Inloggen")
            .performClick()

        composeTestRule.onNodeWithText("Home")
            .assertIsDisplayed()
    }
}




