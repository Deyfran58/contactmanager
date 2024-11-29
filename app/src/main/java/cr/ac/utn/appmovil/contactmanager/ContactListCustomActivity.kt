package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.api.ContactAPI
import cr.ac.utn.appmovil.identities.Contact
import org.json.JSONArray

class ContactListCustomActivity : AppCompatActivity() {

    private val contactAPI = ContactAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list_custom)
        loadContacts()
    }

    private fun loadContacts() {
        try {
            val response = contactAPI.getAllContacts()
            val contacts = JSONArray(response)

            val contactNames = mutableListOf<String>()
            val contactArray = ArrayList<Contact>()

            for (i in 0 until contacts.length()) {
                val contact = contacts.getJSONObject(i)
                val contactObject = Contact(
                    Id = contact.getInt("personId").toString(),
                    Name = contact.getString("name"),
                    LastName = contact.getString("lastName"),
                    Country = contact.getInt("provinceCode").toString()
                )
                contactArray.add(contactObject)
                contactNames.add("${contactObject.Name} ${contactObject.LastName}")
            }

            val lstContactListCustom: ListView = findViewById(R.id.lstContactListCustom)
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactNames)
            lstContactListCustom.adapter = adapter

            lstContactListCustom.setOnItemClickListener { _, _, position, _ ->
                val contact = contactArray[position]
                Toast.makeText(this, "Selected: ${contact.FullName}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
