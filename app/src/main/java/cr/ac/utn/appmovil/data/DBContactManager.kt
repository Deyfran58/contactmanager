package cr.ac.utn.appmovil.data

import android.content.Context
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.identities.ContactDBManager

class DBContactManager (context: Context) : ContactDBManager {
    private val dbHelper = DBhelper(context)

    override fun add(obj: Contact) {
        dbHelper.insertCotact(obj)
    }

    override fun update(obj: Contact) {
        dbHelper.updateCotact(obj)
    }

    override fun remove(id: String) {
        dbHelper.deleteCotactById(id)
    }

    override fun getAll(): List<Contact> {
        return dbHelper.getAllCotact()
    }

    override fun getByid(id: String): Contact? {
        return dbHelper.getCotactById(id)
    }




}