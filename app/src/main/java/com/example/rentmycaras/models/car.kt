package com.example.rentmycaras.models

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    val brand: String,
    val type: String,
    val category: CarCategory,
    val availability: List<TimeBlock>,
//    val rentalConditions: RentalConditions,
    val owner: User,
//    val photos: List<String>
)

enum class CarCategory {
    ICE,
    BEV,
    FCEV
}

data class RentalConditions(
    val price: Double,
    val pickupDropOff: String
)

@Serializable
data class TimeBlock(
    val startTime: Long,
    val endTime: Long
)
