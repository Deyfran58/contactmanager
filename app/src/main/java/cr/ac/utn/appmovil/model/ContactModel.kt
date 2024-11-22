package cr.ac.utn.appmovil.model

import android.content.ContentValues
import android.content.Context
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.db.ContactDbHelper
import cr.ac.utn.appmovil.identities.Contact

class ContactModel(private val _context: Context) {

    private var dbHelper: ContactDbHelper = ContactDbHelper(_context)

    fun addContact(contact: Contact) {
        try {
            dbHelper.insertContact(contact)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(_context.getString(R.string.msgErrorAddingContact))
        }
    }

    fun updateContact(contact: Contact) {
        try {
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put(ContactDbHelper.COLUMN_NAME, contact.Name)
                put(ContactDbHelper.COLUMN_LAST_NAME, contact.LastName)
                put(ContactDbHelper.COLUMN_PHONE, contact.Phone)
                put(ContactDbHelper.COLUMN_EMAIL, contact.Email)
                put(ContactDbHelper.COLUMN_ADDRESS, contact.Address)
                put(ContactDbHelper.COLUMN_COUNTRY, contact.Country)
                put(ContactDbHelper.COLUMN_PHOTO, dbHelper.convertBitmapToByteArray(contact.Photo))
            }

            val rowsUpdated = db.update(
                ContactDbHelper.TABLE_CONTACTS,
                values,
                "${ContactDbHelper.COLUMN_ID} = ?",
                arrayOf(contact.Id)
            )

            if (rowsUpdated == 0) {
                throw Exception(_context.getString(R.string.msgNotFoundContact))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(_context.getString(R.string.msgErrorUpdatingContact))
        }
    }

    fun removeContact(id: String) {
        try {
            val contact = getContact(id)
            val db = dbHelper.writableDatabase
            db.delete(
                ContactDbHelper.TABLE_CONTACTS,
                "${ContactDbHelper.COLUMN_ID} = ?",
                arrayOf(id)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(_context.getString(R.string.msgNotFoundContact))
        }
    }

    fun getContacts(): List<Contact> {
        return dbHelper.getAllContacts()
    }

    fun getContact(id: String): Contact {
        return dbHelper.getContactById(id)
            ?: throw Exception(_context.getString(R.string.msgNotFoundContact))
    }

    fun getContactNames(): List<String> {
        val contacts = dbHelper.getAllContacts()
        return contacts.map { it.FullName }
    }
}
