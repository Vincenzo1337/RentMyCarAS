package com.example.rentmycaras

import com.example.rentmycaras.viewmodels.LoginViewModel
import com.example.rentmycaras.viewmodels.ProfileViewModel
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito

class ProfileViewModelTest {
    @Test
    fun testValidateInput() {
        val loginViewModel = Mockito.mock(LoginViewModel::class.java)
        val viewModel = ProfileViewModel(loginViewModel)
        val result = viewModel.validateInput("ValidPhone", "ValidPassword", "ValidPassword")
        assertTrue(result)
    }
}
