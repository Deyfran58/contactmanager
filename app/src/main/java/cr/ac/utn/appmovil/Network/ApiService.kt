package cr.ac.utn.appmovil.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.io.IOException

class ApiService {

    private val client = OkHttpClient()

    fun getContacts(callback: (String?) -> Unit) {
        val url = "https://movil-vaccine-api.azurewebsites.net/people/"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(response.body?.string())
                } else {
                    callback(null)
                }
            }
        })
    }

    fun getContactById(id: String, callback: (String?) -> Unit) {
        val url = "https://movil-vaccine-api.azurewebsites.net/people/$id"
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    callback(response.body?.string())
                } else {
                    callback(null)
                }
            }
        })
    }

    fun addContact(contact: JSONObject, callback: (Boolean) -> Unit) {
        val url = "https://movil-vaccine-api.azurewebsites.net/people/"
        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), contact.toString())
        val request = Request.Builder().url(url).post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun updateContact(contact: JSONObject, callback: (Boolean) -> Unit) {
        val url = "https://movil-vaccine-api.azurewebsites.net/people/"
        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), contact.toString())
        val request = Request.Builder().url(url).put(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun deleteContact(contact: JSONObject, callback: (Boolean) -> Unit) {
        val url = "https://movil-vaccine-api.azurewebsites.net/people/"
        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), contact.toString())
        val request = Request.Builder().url(url).delete(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }
}
