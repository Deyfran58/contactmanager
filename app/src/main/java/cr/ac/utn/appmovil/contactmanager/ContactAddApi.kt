package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.identities.ContactApi
import cr.ac.utn.appmovil.network.ApiService

class ContactAddApi : AppCompatActivity() {
    private lateinit var etPersonId: EditText
    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etProvinceCode: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var etGender: EditText
    private lateinit var btnAddContact: Button
    private val apiService = ApiService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contact_add_api)

        etPersonId = findViewById(R.id.etPersonId)
        etName = findViewById(R.id.etName)
        etLastName = findViewById(R.id.etLastName)
        etProvinceCode = findViewById(R.id.etProvinceCode)
        etBirthdate = findViewById(R.id.etBirthdate)
        etGender = findViewById(R.id.etGender)
        btnAddContact = findViewById(R.id.btnAddContact)

        btnAddContact.setOnClickListener {
            val personId = etPersonId.text.toString().toIntOrNull()
            val name = etName.text.toString()
            val lastName = etLastName.text.toString()
            val provinceCode = etProvinceCode.text.toString().toIntOrNull()
            val birthdate = etBirthdate.text.toString()
            val gender = etGender.text.toString()

            if (personId != null && provinceCode != null) {
                val contact = ContactApi(personId, name, lastName, provinceCode, birthdate, gender)
                addContact(contact)
            } else {
                Toast.makeText(this, "Please enter valid ID and Province Code", Toast.LENGTH_SHORT).show()
            }
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun addContact(contact: ContactApi) {
        apiService.addContact(contact) { success ->
            runOnUiThread {
                if (success) {
                    Toast.makeText(this, "Contact was added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "There was an error adding the contact", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}



