package Database

import android.content.Context
import cr.ac.utn.appmovil.data.ContactDbHelper
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactDbManager(context: Context): IDBManager {
    private val dbHelper = ContactDbHelper(context)

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