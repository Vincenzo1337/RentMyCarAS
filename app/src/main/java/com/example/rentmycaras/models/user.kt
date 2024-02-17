package com.example.rentmycaras.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val id: Int,
    val drivingBehavior: String
)