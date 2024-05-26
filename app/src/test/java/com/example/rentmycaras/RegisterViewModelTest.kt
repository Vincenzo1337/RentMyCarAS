package com.example.rentmycaras

import com.example.rentmycaras.viewmodels.RegisterViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class RegisterViewModelTest {

    private lateinit var viewModel: RegisterViewModel

    @Before
    fun setUp() {
        viewModel = RegisterViewModel()
    }

    @Test
    fun `validateInput returns error when password fields are empty`() {
        val result = viewModel.validateInput("", "", "name", "phone", "email")
        assertEquals("Wachtwoordvelden mogen niet leeg zijn.", result)
    }

    @Test
    fun `validateInput returns error when passwords do not match`() {
        val result = viewModel.validateInput("password", "differentPassword", "name", "phone", "email")
        assertEquals("Wachtwoorden komen niet overeen.", result)
    }
}
