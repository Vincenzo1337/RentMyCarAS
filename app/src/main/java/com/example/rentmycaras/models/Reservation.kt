
import com.example.rentmycaras.models.TimeBlock
import kotlinx.serialization.Serializable

@Serializable
data class Reservation(
    val userid: Int,
    val carId: Int,
    val timeBlock: TimeBlock,
    val price: Int,
)
enum class ReservationsCategory {
    MONDAY,
    WEDNESDAY,
    FRIDAY
}