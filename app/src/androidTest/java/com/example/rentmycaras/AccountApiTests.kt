package com.example.rentmycaras

import Reservation
import com.example.rentmycaras.api.CarApiService
import com.example.rentmycaras.models.Account
import com.example.rentmycaras.models.Car
import com.example.rentmycaras.models.CarCategory
import com.example.rentmycaras.models.TimeBlock
import com.example.rentmycaras.models.User
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class AccountApiTests {

    class FakeCarApiService : CarApiService {
        private lateinit var response: Response<Unit>

        fun setExpectedResponse(response: Response<Unit>) {
            this.response = response
        }

        override suspend fun getAllUsers(): List<User> {

            return emptyList()
        }

        override suspend fun getUserById(id: Int): List<User> {
            return emptyList()
        }

        override suspend fun getAllCars(search: String?): List<Car> {
            return emptyList()
        }

        override suspend fun getCarById(id: Int): Response<Car> {
            val mockCar = Car(
                id = 1,
                brand = "Test Brand",
                type = "Test Type",
                category = CarCategory.ICE,
                availability = true,
                timeBlock = listOf(TimeBlock(0, 0)),
                description = "Test Description",
                ownerId = 1,
                owner = null
            )

            return Response.success(mockCar)
        }

        override suspend fun cars(car: Car): Response<Unit> {
            return Response.success(Unit)
        }

        override suspend fun login(account: Account): Response<Unit> {
            return response
        }

        override suspend fun register(account: Account): Response<Unit> {
            return Response.success(Unit)
        }

        override suspend fun getAccountByUsername(username: String): Response<Account> {
            val mockAccount = Account(
                userName = "testUserName",
                password = "testPassword",
                userId = 1,
                phone = "testPhone",
                email = "testEmail"
            )

            return Response.success(mockAccount)
        }

        override suspend fun updateAccount(username: String, account: Account): Response<Unit> {
            return Response.success(Unit)
        }

        override suspend fun getAvailability(carId: Int): Response<Boolean> {
            return Response.success(true)
        }

        override suspend fun createReservation(reservation: Reservation): Response<ResponseBody> {
            return Response.success(ResponseBody.create(null, ""))
        }

        override suspend fun getReservationsByUser(userid: Int): List<Reservation> {
            return emptyList()
        }
    }

    @Test
    fun testLoginFunction() = runBlocking {
        val mockAccount = Account("testUserName", "testPassword", 1, "testPhone", "testEmail")

        val fakeCarApiService = FakeCarApiService()

        val expectedResponse = Response.success(Unit)

        fakeCarApiService.setExpectedResponse(expectedResponse)

        val response = fakeCarApiService.login(mockAccount)

        assertEquals(expectedResponse, response)
    }

    @Test
    fun testUpdateAccount() = runBlocking {

        val mockAccount = Account("testUserName", "testPassword", 1, "testPhone", "testEmail")

        val fakeCarApiService = FakeCarApiService()

        val expectedResponse = Response.success(Unit)

        fakeCarApiService.setExpectedResponse(expectedResponse)

        val actualResponse = fakeCarApiService.updateAccount(mockAccount.userName, mockAccount)

        assertEquals(expectedResponse.code(), actualResponse.code())
        assertEquals(expectedResponse.message(), actualResponse.message())
        assertEquals(expectedResponse.isSuccessful, actualResponse.isSuccessful)
        assertEquals(expectedResponse.body(), actualResponse.body())
    }

    @Test
    fun testRegisterAccount() = runBlocking {

        val mockAccount = Account("testUserName", "testPassword", 1, "testPhone", "testEmail")

        val fakeCarApiService = FakeCarApiService()

        val expectedResponse = Response.success(Unit)

        fakeCarApiService.setExpectedResponse(expectedResponse)

        val response = fakeCarApiService.register(mockAccount)

        assertEquals(expectedResponse.code(), response.code())
        assertEquals(expectedResponse.message(), response.message())
        assertEquals(expectedResponse.isSuccessful, response.isSuccessful)
        assertEquals(expectedResponse.body(), response.body())
    }

}
