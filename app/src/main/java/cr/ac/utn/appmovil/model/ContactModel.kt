package cr.ac.utn.appmovil.model
import android.content.Context
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.data.DatabaseHelper
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class ContactModel (private var dbManager: IDBManager, private var _context: Context) {

    constructor(context: Context) : this(DatabaseHelper(context), context)

    fun addContact(contact: Contact){
        dbManager.addContact(contact)
    }

    fun updateContact(contact: Contact){
        dbManager.updateContact(contact)
    }

    fun removeContact(id: String){
        var result = dbManager.getContactById(id)
        if (result == null)
            throw Exception(Resources.getSystem().getString(R.string.msgNotFoundContact))

        dbManager.removeContact(id)
    }

    fun getAllContact(): List<Contact> {
        // Lógica para obtener la lista de contactos
        return listOf() // Sustituir con la lógica real
    }

    fun getContacts() = getAllContact()

    fun getContact(id: String): Contact{
        var result = dbManager.getContactById(id)
        if (result == null){
            val message = _context.getString(R.string.msgNotFoundContact).toString()
            throw Exception(message)
        }
        return result
    }

    fun getContactNames(): List<String>{
        val names = mutableListOf<String>()
        val contacts = dbManager.getAllContact()
        contacts.forEach { i-> names.add(i._name + " " + i._lastName)  }
        return names.toList()
    }

}