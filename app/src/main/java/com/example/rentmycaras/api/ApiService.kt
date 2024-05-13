package com.example.rentmycaras.api

import Reservation
import com.example.rentmycaras.models.Account
import com.example.rentmycaras.models.Car
import com.example.rentmycaras.models.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL: String = "http://192.168.178.39:8080"
private val contentType = "application/json".toMediaType()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(contentType))
    .baseUrl(BASE_URL)
    .build()

interface CarApiService {
    @GET("/users")
    suspend fun getAllUsers(): List<User>

    @GET("/user/{id}")
    suspend fun getUserById(@Path("id") id: Int): List<User>

    // /cars?search=BMW
    @GET("/cars")
    suspend fun getAllCars(@Query("search") search: String? = null): List<Car>

    @GET("/car/{id}")
    suspend fun getCarById(@Path("id") id: String): Response<Car>

    @POST("/login")
    suspend fun login(@Body account: Account): Response<Unit>

    @POST("/register")
    suspend fun register(@Body account: Account): Response<Unit>

    @GET("/account/{username}")
    suspend fun getAccountByUsername(@Path("username") username: String): Response<Account>

    @PUT("/account/{username}")
    suspend fun updateAccount(@Path("username") username: String, @Body account: Account): Response<Unit>

    @GET("/reservations/{car_id}/availability")
    suspend fun getAvailability(@Path("car_id") carId: Int): Response<Boolean>

    @POST("/reservations")
    suspend fun createReservation(@Body reservation: Reservation): Response<ResponseBody>

}

object CarApi {
    // todo
    val carApiService: CarApiService by lazy {
        retrofit.create(CarApiService::class.java)
    }
}
