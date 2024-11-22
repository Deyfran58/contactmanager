package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class SQLiteManager(context: Context): IDBManager {

    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private var db: SQLiteDatabase? = null

    init {
        db = dbHelper.writableDatabase
    }

    override fun add(contact: Contact) {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ID, contact.Id)
            put(DatabaseHelper.COLUMN_NAME, contact.Name)
            put(DatabaseHelper.COLUMN_LASTNAME, contact.LastName)
            put(DatabaseHelper.COLUMN_PHONE, contact.Phone)
            put(DatabaseHelper.COLUMN_EMAIL, contact.Email)
            put(DatabaseHelper.COLUMN_ADDRESS, contact.Address)
            put(DatabaseHelper.COLUMN_COUNTRY, contact.Country)
            put(DatabaseHelper.COLUMN_PHOTO, contact.PhotoByteArray)
        }
        db?.insert(DatabaseHelper.TABLE_CONTACTS, null, values)
    }

    override fun update(contact: Contact) {
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_NAME, contact.Name)
            put(DatabaseHelper.COLUMN_LASTNAME, contact.LastName)
            put(DatabaseHelper.COLUMN_PHONE, contact.Phone)
            put(DatabaseHelper.COLUMN_EMAIL, contact.Email)
            put(DatabaseHelper.COLUMN_ADDRESS, contact.Address)
            put(DatabaseHelper.COLUMN_COUNTRY, contact.Country)
            put(DatabaseHelper.COLUMN_PHOTO, contact.PhotoByteArray)
        }
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(contact.Id)
        db?.update(DatabaseHelper.TABLE_CONTACTS, values, selection, selectionArgs)
    }

    override fun remove(id: String) {
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id)
        db?.delete(DatabaseHelper.TABLE_CONTACTS, selection, selectionArgs)
    }

    override fun getAll(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = db?.query(
            DatabaseHelper.TABLE_CONTACTS,
            null,
            null,
            null,
            null,
            null,
            null
        )
        with(cursor) {
            while (this != null && moveToNext()) {
                val contact = Contact().apply {
                    Id = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                    Name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                    LastName = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_LASTNAME))
                    Phone = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE))
                    Email = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL))
                    Address = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS))
                    Country = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_COUNTRY))
                    PhotoByteArray = getBlob(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHOTO))
                }
                contacts.add(contact)
            }
        }
        cursor?.close()
        return contacts
    }

    override fun getById(id: String): Contact? {
        val selection = "${DatabaseHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id)
        val cursor = db?.query(
            DatabaseHelper.TABLE_CONTACTS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        var contact: Contact? = null
        with(cursor) {
            if (this != null && moveToFirst()) {
                contact = Contact().apply {
                    Id = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
                    Name = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME))
                    LastName = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_LASTNAME))
                    Phone = getInt(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE))
                    Email = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL))
                    Address = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_ADDRESS))
                    Country = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_COUNTRY))
                    PhotoByteArray = getBlob(getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHOTO))
                }
            }
        }
        cursor?.close()
        return contact
    }
}
