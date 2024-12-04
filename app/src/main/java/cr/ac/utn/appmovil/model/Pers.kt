package cr.ac.utn.appmovil.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
class Pers (
    @SerializedName("personId")
    val personId: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("provinceCode")
    val provinceCode: String? = null,

    @SerializedName("birthdate")
    val birthdate: String? = null,

    @SerializedName("gender")
    val gender: String? = null,

    @SerializedName("lat")
    val lat: Double? = null,

    @SerializedName("long")
    val long: Double? = null,

    @SerializedName("address")
    val address: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("phone")
    val phone: Long? = null
) : Serializable