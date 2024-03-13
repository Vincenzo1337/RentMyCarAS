package com.example.rentmycaras.models

import kotlinx.serialization.Serializable

// todo: wanneer bijv owner is null, laat dan niet zien
@Serializable
data class Car(
    val brand: String,
    val type: String,
    val category: CarCategory,
    val availability: Boolean,
    val timeBlock: List<TimeBlock> = emptyList(),
    val owner: User = User(),
//    val photos: List<String>
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
