package cr.ac.utn.appmovil.model

import android.content.Context
import androidx.core.content.ContextCompat.getString
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.data.SQLiteDBManager
import cr.ac.utn.appmovil.identities.Contact

class ContactModel(context: Context) {
    private var dbManager = SQLiteDBManager(context)

    fun addContact(contact: Contact) {
        dbManager.add(contact)
    }

    fun updateContact(contact: Contact) {
        dbManager.update(contact)
    }

    fun removeContact(id: String) {
        val result = dbManager.getById(id)
        dbManager.remove(id)
    }

    fun getContacts() = dbManager.getAll()

    fun getContact(id: String): Contact? {
        val result = dbManager.getById(id)
        return result
    }

    fun getContactNames(): List<String> {
        val names = mutableListOf<String>()
        val contacts = dbManager.getAll()
        contacts.forEach { i -> names.add(i.FullName) }
        return names.toList()
    }
}
