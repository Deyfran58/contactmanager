package cr.ac.utn.appmovil.identities

import org.json.JSONObject

data class Contact(
    val Id: String,
    val FullName: String,
    val Address: String,
    val Phone: String,
    val Photo: String? = null // Foto opcional
) {
    fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("Id", Id)
        jsonObject.put("FullName", FullName)
        jsonObject.put("Address", Address)
        jsonObject.put("Phone", Phone)
        jsonObject.put("Photo", Photo)
        return jsonObject
    }

    companion object {
        fun fromJson(json: JSONObject): Contact {
            return Contact(
                Id = json.getString("Id"),
                FullName = json.getString("FullName"),
                Address = json.getString("Address"),
                Phone = json.getString("Phone"),
                Photo = json.optString("Photo", null) // Maneja nulos
            )
        }
    }
}
