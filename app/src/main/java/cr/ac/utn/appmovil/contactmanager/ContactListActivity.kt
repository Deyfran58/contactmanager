package cr.ac.utn.appmovil.contactmanager

import adapter.ContactAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import cr.ac.utn.appmovil.data.DatabaseHelper
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.model.ContactModel

class ContactListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        val dbHelper = DatabaseHelper(this)
        val contacts: List<Contact> = dbHelper.getAllContact()

        val lstContactList = findViewById<ListView>(R.id.lstContactList)
        val adapter = ContactAdapter(this, contacts)
        lstContactList.adapter = adapter


        lstContactList.setOnItemClickListener { _, _, position, _ ->
            val selectedService = contacts[position]
            // Manejar el clic en el servicio seleccionado
        }

    }
}