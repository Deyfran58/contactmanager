package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.network.ApiService
import cr.ac.utn.appmovil.identities.ContactAPI
import org.json.JSONArray
import org.json.JSONObject

class listContactAPI : AppCompatActivity() {
    private lateinit var listViewContacts: ListView
    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_list_contact_api)

        try {
            listViewContacts = findViewById(R.id.listViewContacts)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error initializing views: ${e.message}", Toast.LENGTH_LONG).show()
        }

        try {
            fetchContacts()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error fetching contacts: ${e.message}", Toast.LENGTH_LONG).show()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun fetchContacts() {
        apiService.getContacts { response ->
            runOnUiThread {
                response?.let {
                    try {
                        val contacts = parseContacts(it)
                        val contactStrings = contacts.map { contact -> "${contact.name} ${contact.lastName}" }
                        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactStrings)
                        listViewContacts.adapter = adapter
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Error parsing contacts: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                } ?: run {
                    Toast.makeText(this, "Failed to load contacts", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun parseContacts(json: String): List<ContactAPI> {
        val jsonObject = JSONObject(json)
        val jsonArray = jsonObject.getJSONArray("data")
        val contactList = mutableListOf<ContactAPI>()

        for (i in 0 until jsonArray.length()) {
            val contactJson = jsonArray.getJSONObject(i)


            val provinceCode: Any = try {
                contactJson.getInt("provinceCode")
            } catch (e: Exception) {
                contactJson.getString("provinceCode")
            }


            val birthdate: String = if (contactJson.has("birthdate")) {
                contactJson.getString("birthdate")
            } else {
                ""
            }

            val gender: String = if (contactJson.has("gender")) {
                contactJson.getString("gender")
            } else {
                ""
            }

            val contact = ContactAPI(
                contactJson.getInt("personId"),
                contactJson.getString("name"),
                contactJson.getString("lastName"),
                provinceCode,
                birthdate,
                gender
            )
            contactList.add(contact)
        }
        return contactList
    }



}
