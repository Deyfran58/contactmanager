package cr.ac.utn.appmovil.identities

data class LoginEvent (
    val eventId: Int = 0,
    val userId: String,
    val loginTime: String
)