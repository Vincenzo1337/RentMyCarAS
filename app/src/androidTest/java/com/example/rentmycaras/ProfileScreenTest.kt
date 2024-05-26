package com.example.rentmycaras

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.rememberNavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rentmycaras.screens.ProfileScreen
import com.example.rentmycaras.viewmodels.LoginViewModel
import com.example.rentmycaras.viewmodels.ProfileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<TestActivity>()

    class TestActivity : ComponentActivity()
    @Test
    fun testProfileScreenIsDisplayed() {
        // Mock the ViewModels
        val mockLoginViewModel = mock(LoginViewModel::class.java)
        val mockProfileViewModel = mock(ProfileViewModel::class.java)

        // Set up the mock ViewModels
        `when`(mockLoginViewModel.loggedInUser).thenReturn(MutableLiveData("testUsername"))
        `when`(mockLoginViewModel.loggedInEmail).thenReturn(MutableLiveData("testEmail"))
        `when`(mockProfileViewModel.validationError).thenReturn(MutableStateFlow(null))
        `when`(mockProfileViewModel.updateSuccess).thenReturn(MutableStateFlow(null))

        composeTestRule.setContent {
            ProfileScreen(navController = rememberNavController(), loginViewModel = mockLoginViewModel, profileViewModel = mockProfileViewModel)
        }

        // Controleer of de gebruikersnaam en e-mail correct worden weergegeven
        composeTestRule.onNodeWithText("testUsername").assertIsDisplayed()
        composeTestRule.onNodeWithText("testEmail").assertIsDisplayed()
    }
}

