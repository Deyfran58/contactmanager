package cr.ac.utn.appmovil.model
import android.content.Context
import android.content.res.Resources
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.data.DBContactManager
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.identities.ContactDBManager

class ContactModel(context: Context) {

    private lateinit var _context: Context
    private var dbManager: ContactDBManager

    init {
        _context = context  // Inicializar _context dentro del bloque init
        dbManager = DBContactManager(_context)  // Inicializar dbManager después de que _context esté inicializado
    }

    fun addContact(contact: Contact) {
        dbManager.add(contact)
    }

    fun updateContact(contact: Contact) {
        dbManager.update(contact)
    }

    fun removeContact(id: String) {
        val result = dbManager.getByid(id)
        if (result == null)
            throw Exception(Resources.getSystem().getString(R.string.msgNotFoundContact))

        dbManager.remove(id)
    }

    fun getContacts() = dbManager.getAll()

    fun getContact(id: String): Contact {
        val result = dbManager.getByid(id)
        if (result == null) {
            val message = _context.getString(R.string.msgNotFoundContact).toString()
            throw Exception(message)
        }
        return result
    }

    fun getContactNames(): List<String> {
        val names = mutableListOf<String>()
        val contacts = dbManager.getAll()
        contacts.forEach { i -> names.add(i.FullName) }
        return names.toList()
    }
}
