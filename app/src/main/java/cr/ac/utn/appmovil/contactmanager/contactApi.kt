package cr.ac.utn.appmovil.api

import cr.ac.utn.appmovil.identities.Contact
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class ContactAPI {

    // Obtener un contacto específico por ID
    fun getContactById(id: String): JSONObject? {
        val url = URL("https://movil-vaccine-api.azurewebsites.net/people/$id")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Accept", "application/json")

        return try {
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                JSONObject(response)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            connection.disconnect()
        }
    }

    // Agregar un nuevo contacto
    fun addContact(contact: Contact): String {
        val url = URL("https://movil-vaccine-api.azurewebsites.net/people/")
        return sendPostRequest(url, contact.toJson())
    }

    // Actualizar un contacto existente
    fun updateContact(contact: Contact): String {
        val url = URL("https://movil-vaccine-api.azurewebsites.net/people/")
        return sendPostRequest(url, contact.toJson())
    }

    // Eliminar un contacto por ID
    fun deleteContact(id: String): String {
        val url = URL("https://movil-vaccine-api.azurewebsites.net/people/$id")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "DELETE"
        connection.setRequestProperty("Accept", "application/json")

        return try {
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                "Success"
            } else {
                "Error: ${connection.responseMessage}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        } finally {
            connection.disconnect()
        }
    }

    // Método para enviar una solicitud POST
    private fun sendPostRequest(url: URL, json: JSONObject): String {
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        return try {
            val outputStream: OutputStream = connection.outputStream
            outputStream.write(json.toString().toByteArray(Charsets.UTF_8))
            outputStream.flush()
            outputStream.close()

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                "Success"
            } else {
                "Error: ${connection.responseMessage}"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: ${e.message}"
        } finally {
            connection.disconnect()
        }
    }
}
