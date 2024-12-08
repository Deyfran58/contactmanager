package cr.ac.utn.appmovil.identities

data class ContactApi(
    val personId: Int,
    val name: String,
    val lastName: String,
    val provinceCode: Any,
    val birthdate: String,
    val gender: String
)
