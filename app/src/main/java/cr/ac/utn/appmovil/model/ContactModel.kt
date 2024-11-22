package cr.ac.utn.appmovil.model

import android.content.Context
import cr.ac.utn.appmovil.data.DbManager
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactModel(context: Context) {
    private var dbManager: IDBManager = DbManager(context)

    fun addContact(contact: Contact) {
        dbManager.add(contact)
    }

    fun updateContact(contact: Contact) {
        dbManager.update(contact)
    }

    fun removeContact(id: String) {
        dbManager.remove(id)
    }

    fun getContacts(): List<Contact> = dbManager.getAll()

    fun getContact(id: String): Contact? = dbManager.getById(id)

    fun getContactNames(): List<String> {
        return getContacts().map { it.FullName }
    }
}
