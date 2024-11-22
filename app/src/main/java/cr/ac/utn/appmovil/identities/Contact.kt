package cr.ac.utn.appmovil.identities

import android.graphics.Bitmap

data class Contact (
    var _id: String,
    var _name: String,
    var _lastName: String,
    var _phone: Int,
    var _email: String,
    var _address: String,
    var _country: String,
    var _photo: Bitmap? = null


)


