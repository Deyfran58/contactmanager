package cr.ac.utn.appmovil.data


import android.content.Context
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactDBManager(context: Context) : IDBManager {
    private val dbHelper = ContactDBHelper(context)

    override fun add(obj: Contact) {
        dbHelper.addContact(obj)
    }

    override fun update(obj: Contact) {
        dbHelper.modifyContact(obj)
    }

    override fun remove(id: String) {
        dbHelper.removeContactById(id)
    }

    override fun getAll(): List<Contact> {
        return dbHelper.fetchAllContacts()
    }

    override fun getById(id: String): Contact? {
        return dbHelper.fetchContactById(id)
    }

    override fun getByFullName(fullName: String): Contact? {
        return getAll().firstOrNull { "${it.Name} ${it.LastName}" == fullName }
    }
}