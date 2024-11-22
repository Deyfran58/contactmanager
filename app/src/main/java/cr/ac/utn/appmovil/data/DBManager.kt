package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.appmovil.util.util



class DBManager(context: Context) : IDBManager {

    private val dbhelperlite: ContactDatabaseHelper = ContactDatabaseHelper(context)




    override fun add(contact: Contact) {
        val db = dbhelperlite.writableDatabase
        val values = ContentValues().apply {
            put("id", contact.Id)
            put("name", contact.Name)
            put("lastname", contact.LastName)
            put("phone", contact.Phone)
            put("email", contact.Email)
            put("address", contact.Address)
            put("country", contact.Country)
            contact.Photo?.let {
                put("photo", util.convertToByteArray(it))
            }
        }
        db.insert("contacts", null, values)
        db.close()
    }





    override fun update(contact: Contact) {
        val db = dbhelperlite.writableDatabase
        val values = ContentValues().apply {
            put("name", contact.Name)
            put("lastname", contact.LastName)
            put("phone", contact.Phone)
            put("email", contact.Email)
            put("address", contact.Address)
            put("country", contact.Country)
            contact.Photo?.let {
                put("photo", util.convertToByteArray(it))
            }
        }
        db.update("contacts", values, "id = ?", arrayOf(contact.Id))
        db.close()
    }

    override fun remove(id: String) {
        val db = dbhelperlite.writableDatabase
        db.delete("contacts", "id = ?", arrayOf(id))
        db.close()
    }



    override fun getAll(): List<Contact> {
        val db = dbhelperlite.readableDatabase
        val cursor: Cursor = db.query("contacts", null, null, null, null, null, null)
        val contacts = mutableListOf<Contact>()

        with(cursor) {
            while (moveToNext()) {
                val contact = Contact(
                    getString(getColumnIndexOrThrow("id")),
                    getString(getColumnIndexOrThrow("name")),
                    getString(getColumnIndexOrThrow("lastname")),
                    getInt(getColumnIndexOrThrow("phone")),
                    getString(getColumnIndexOrThrow("email")),
                    getString(getColumnIndexOrThrow("address")),
                    getString(getColumnIndexOrThrow("country")),
                    getBlob(getColumnIndexOrThrow("photo"))?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }!!
                )
                contacts.add(contact)
            }
        }
        cursor.close()
        db.close()
        return contacts
    }

    override fun getById(id: String): Contact? {
        val db = dbhelperlite.readableDatabase
        val cursor: Cursor = db.query("contacts", null, "id = ?", arrayOf(id), null, null, null)

        var contact: Contact? = null

        with(cursor) {
            if (moveToFirst()) {
                contact = Contact(
                    getString(getColumnIndexOrThrow("id")),
                    getString(getColumnIndexOrThrow("name")),
                    getString(getColumnIndexOrThrow("lastname")),
                    getInt(getColumnIndexOrThrow("phone")),
                    getString(getColumnIndexOrThrow("email")),
                    getString(getColumnIndexOrThrow("address")),
                    getString(getColumnIndexOrThrow("country")),
                    getBlob(getColumnIndexOrThrow("photo"))?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }!!
                )
            }
        }
        cursor.close()
        db.close()
        return contact
    }
}
