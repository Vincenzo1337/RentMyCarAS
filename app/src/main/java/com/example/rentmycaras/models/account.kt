package com.example.rentmycaras.models

import kotlinx.serialization.Serializable

@Serializable
data class Account (
    val userName: String,
    val password: String,
    val userId: Int,
    val name: String,
    val phone: String,
    val email: String
)