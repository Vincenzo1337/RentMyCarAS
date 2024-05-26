package com.example.rentmycaras

import com.example.rentmycaras.api.CarApi
import com.example.rentmycaras.models.Account
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CarApiServiceTest {
    @Test
    fun testRegister() = runBlocking {
        val uniqueUsername = "testUsername" + System.currentTimeMillis()
        val account = Account(uniqueUsername, "testPassword", 1, "testPhone", "testEmail")
        val response = CarApi.carApiService.register(account)
        assertEquals(true, response.isSuccessful)
    }

    @Test
    fun testGetAllUsers() = runBlocking {
        val response = CarApi.carApiService.getAllUsers()
        assertEquals(true, response.isNotEmpty())
    }

    @Test
    fun testGetAllCars() = runBlocking {
        val response = CarApi.carApiService.getAllCars()
        assertEquals(true, response.isNotEmpty())
    }

    @Test
    fun testGetCarById() = runBlocking {
        val carId = 1 // Vervang dit door een geldig auto-ID in je database
        val response = CarApi.carApiService.getCarById(carId)
        assertEquals(true, response.isSuccessful)
        assertEquals(carId, response.body()?.id)
    }

}
