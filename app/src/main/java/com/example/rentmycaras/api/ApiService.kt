package com.example.rentmycaras.api

import com.example.rentmycaras.models.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL: String = "http://192.168.2.4:8080"
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

//    @POST("/add/user")
//    suspend fun createNewUser(@Body todo: User): User
}

object CarApi {
    // todo
    val carApiService: CarApiService by lazy {
        retrofit.create(CarApiService::class.java)
    }
}
