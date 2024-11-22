package cr.ac.utn.appmovil.model

import android.content.Context
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.data.ContactDataBaseHelper
import cr.ac.utn.appmovil.data.DBManager
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactModel(private val context: Context) {

    private lateinit var dbManager: IDBManager
    private lateinit var _context: Context


    fun addContact(contact: Contact) {
        dbManager.add(contact)
    }

    fun updateContact(contact: Contact) {
        dbManager.update(contact)
    }

    fun removeContact(id: String) {
        val result = dbManager.getById(id)
        if (result == null) {
            throw Exception(context.getString(R.string.msgNotFoundContact))
        }
        dbManager.delete(id)
    }

    fun getContacts(): List<Contact> {
        return dbManager.getAll()
    }

    fun getContact(id: String): Contact {
        val result = dbManager.getById(id)
        if (result == null) {
            throw Exception(context.getString(R.string.msgNotFoundContact))
        }
        return result
    }

    fun getContactNames(): List<String> {
        return dbManager.getAll().map { it.FullName }
    }
}
