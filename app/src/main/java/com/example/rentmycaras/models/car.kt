package com.example.rentmycaras.models

import com.google.android.gms.maps.model.LatLng
import kotlinx.serialization.Serializable

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
    val owner: User? = null,
    val location: Location? = null,
    val isNew: Boolean = false
)

@Serializable
data class Location(val latitude: Double, val longitude: Double)

fun Location.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

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
