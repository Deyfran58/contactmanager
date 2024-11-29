package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.api.ContactAPI
import cr.ac.utn.appmovil.identities.Contact
import org.json.JSONArray

class ContactListActivity : AppCompatActivity() {

    private val contactAPI = ContactAPI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)
        loadContacts()
    }

    private fun loadContacts() {
        try {
            // Llama al API para obtener todos los contactos
            val response = contactAPI.getAllContacts()
            val contacts = JSONArray(response)

            val contactNames = mutableListOf<String>()
            val contactArray = ArrayList<Contact>() // Lista de contactos

            // Procesar la respuesta JSON
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

            // Configurar el adaptador del ListView
            val listView: ListView = findViewById(R.id.lstContactList)
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, contactNames)
            listView.adapter = adapter

            // Manejar clics en los elementos de la lista
            listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                val selectedContact = contactArray[position]
                Toast.makeText(this, "Selected: ${selectedContact.FullName}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // Manejar errores
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
