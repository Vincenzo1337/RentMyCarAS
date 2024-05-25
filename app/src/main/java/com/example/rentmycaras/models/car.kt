package com.example.rentmycaras.models

import kotlinx.serialization.Serializable

// todo: wanneer bijv owner is null, laat dan niet zien
@Serializable
data class Car(
    val id: Int? = null,
    val brand: String,
    val type: String,
    val category: CarCategory,
    val availability: Boolean,
    val timeBlock: List<TimeBlock>,
    val description: String,
    val ownerId: Int,
    val owner: User? = null
)


enum class CarCategory {
    ICE,
    BEV,
    FCEV
}

@Serializable
data class TimeBlock(
    val startTime: Long,
    val endTime: Long
)
