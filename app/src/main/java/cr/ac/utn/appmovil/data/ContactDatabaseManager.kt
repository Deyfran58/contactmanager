package Database

import android.content.Context
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import data.DatabaseHelper

class ContactDatabaseManager(context: Context) : IDBManager {
    private val dbHelper = DatabaseHelper(context)

    override fun add(contact: Contact) {
        dbHelper.add(contact)
    }

    override fun update(contact: Contact) {
        dbHelper.update(contact)
    }

    override fun remove(id: String) {
        dbHelper.remove(id)
    }

    override fun getAll(): List<Contact> {
        return dbHelper.getAll()
    }

    override fun getById(id: String): Contact? {
        return dbHelper.getById(id)
    }
}
