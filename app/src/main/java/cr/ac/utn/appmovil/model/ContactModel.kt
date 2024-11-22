package cr.ac.utn.appmovil.model

import android.content.Context
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.appmovil.identities.Contact
import database.ContactDBManager // Asegúrate de importar correctamente tu ContactDBManager

class ContactModel(private var dbManager: IDBManager, private var context: Context) {

    constructor(context: Context) : this(ContactDBManager(context), context)


    fun addContact(contact: Contact) {
        dbManager.add(contact)
    }

    fun updateContact(contact: Contact) {
        dbManager.update(contact)
    }


    fun removeContact(id: String) {
        val result = dbManager.getById(id)
        if (result == null) {
            val message = context.getString(R.string.msgNotFoundContact)
            throw Exception(message)
        }
        dbManager.remove(id)
    }


    fun getAllContacts(): List<Contact> {
        return dbManager.getAll()
    }

    fun getContacts() = getAllContacts()


    fun getContact(id: String): Contact {
        val result = dbManager.getById(id)
        if (result == null) {
            val message = context.getString(R.string.msgNotFoundContact)
            throw Exception(message)
        }
        return result
    }

    fun getContactNames(): List<String> {
        val names = mutableListOf<String>()
        val contacts = dbManager.getAll()
        contacts.forEach { contact ->
            names.add("${contact.Name} ${contact.LastName}")
        }
        return names
    }
}
