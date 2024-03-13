package com.example.rentmycaras.models


import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

//@Serializable
//data class User(
//    val name: String,
//    val email: String,
//    val password: String,
//    val role: Role,
//    val id: Int,
//    val drivingBehavior: String
//)

//@Serializable(with = RoleSerializer::class)
//enum class Role {
//    OWNER,
//    RENTER
//}

//object RoleSerializer : KSerializer<Role> {
//    override val descriptor = String.serializer().descriptor
//
//    override fun deserialize(decoder: Decoder): Role {
//        return when (val value = decoder.decodeString()) {
//            "OWNER" -> Role.OWNER
//            "RENTER" -> Role.RENTER
//            else -> throw IllegalArgumentException("Unknown Role: $value")
//        }
//    }
//
//    override fun serialize(encoder: Encoder, value: Role) {
//        encoder.encodeString(value.name)
//    }
//}


@Serializable
data class User(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val role: Role? = null,
    val id:  Int? = null,
    val drivingBehavior: DrivingBehavior? = null,
)

enum class Role {
    OWNER,
    RENTER
}

data class RentalAgreement(
    val renter: User,
    val selectedTimeBlock: TimeBlock,
    val bonusPoints: Int
) {
    // fun requestRoute() {
    //     // Implement logic to request a route
    // }

    // fun awardBonusPoints() {
    //     // Implement logic to award bonus points
    // }
}

data class BonusPoints(
    val points: Int
) {
    // fun awardPoints() {
    //     // Implement logic to award bonus points
    // }
}

//data class DrivingBehavior(
//    val distanceDriven: Double,
//    val accelerationOnStart: Double,
//    val deceleration: Double
//)
enum class DrivingBehavior{
    GOOD,
    BAD,
    NONE

}
