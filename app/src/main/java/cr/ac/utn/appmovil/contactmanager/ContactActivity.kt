package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.api.ContactAPI
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.contactmanager.databinding.ActivityContactBinding

class ContactActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactBinding
    private val contactAPI = ContactAPI()
    private var isEditionMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar el binding
        binding = ActivityContactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar listeners para botones
        binding.btnSave.setOnClickListener {
            saveContact()
        }

        binding.btnDelete.setOnClickListener {
            deleteContact()
        }

        // Si existe un id de contacto, cargar datos
        val contactId = intent.getStringExtra("contactId") ?: return
        loadContact(contactId)
    }

    private fun loadContact(id: String): Boolean {
        val contactJson = contactAPI.getContactById(id)
        return if (contactJson != null) {
            val contact = Contact.fromJson(contactJson)
            binding.txtContactId.setText(contact.Id)
            binding.txtContactName.setText(contact.FullName)
            binding.txtContactLastName.setText(contact.Address)
            binding.spCountriesContact.setSelection(contact.Phone.toInt() - 1)
            isEditionMode = true
            binding.txtContactId.isEnabled = false
            true
        } else {
            Toast.makeText(this, "Contact not found", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun saveContact() {
        val contact = Contact(
            Id = binding.txtContactId.text.toString(),
            FullName = binding.txtContactName.text.toString(),
            Address = binding.txtContactLastName.text.toString(),
            Phone = (binding.spCountriesContact.selectedItemPosition + 1).toString()
        )

        val success = if (isEditionMode) {
            contactAPI.updateContact(contact)
        } else {
            contactAPI.addContact(contact)
        }

        if (success == "Success") {
            Toast.makeText(this, "Contact saved successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error saving contact: $success", Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteContact() {
        val success = contactAPI.deleteContact(binding.txtContactId.text.toString())
        if (success == "Success") {
            Toast.makeText(this, "Contact deleted successfully", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error deleting contact: $success", Toast.LENGTH_LONG).show()
        }
    }
}
