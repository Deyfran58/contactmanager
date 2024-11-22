package Database

import android.content.Context
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactDatabaseManager(context: Context) : IDBManager {
    private val dbHelper = ContactDatabaseHelper(context)

    override fun add(obj: Contact) {
        dbHelper.insertContact(obj)
    }

    override fun update(obj: Contact) {
        dbHelper.updateContact(obj)
    }

    override fun remove(id: String) {
        dbHelper.deleteContactById(id)
    }

    override fun getAll(): List<Contact> {
        return dbHelper.getAllContacts()
    }

    override fun getById(id: String): Contact? {
        return dbHelper.getContactById(id)
    }

    override fun getByFullName(fullName: String): Contact? {
        return getAll().find { it.FullName == fullName }
    }
}
