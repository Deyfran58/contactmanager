package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager

class SQLiteManager(context: Context): IDBManager {

    private val dbHelper: DBManager = DBManager(context)
    private var db: SQLiteDatabase? = null

    init {
        db = dbHelper.writableDatabase
    }

    override fun add(contact: Contact) {
        val values = ContentValues().apply {
            put(DBManager.COLUMN_ID, contact.Id)
            put(DBManager.COLUMN_NAME, contact.Name)
            put(DBManager.COLUMN_LASTNAME, contact.LastName)
            put(DBManager.COLUMN_PHONE, contact.Phone)
            put(DBManager.COLUMN_EMAIL, contact.Email)
            put(DBManager.COLUMN_ADDRESS, contact.Address)
            put(DBManager.COLUMN_COUNTRY, contact.Country)
            put(DBManager.COLUMN_PHOTO, contact.PhotoByteArray)
        }
        db?.insert(DBManager.TABLE_CONTACTS, null, values)
    }

    override fun update(contact: Contact) {
        val values = ContentValues().apply {
            put(DBManager.COLUMN_NAME, contact.Name)
            put(DBManager.COLUMN_LASTNAME, contact.LastName)
            put(DBManager.COLUMN_PHONE, contact.Phone)
            put(DBManager.COLUMN_EMAIL, contact.Email)
            put(DBManager.COLUMN_ADDRESS, contact.Address)
            put(DBManager.COLUMN_COUNTRY, contact.Country)
            put(DBManager.COLUMN_PHOTO, contact.PhotoByteArray)
        }
        val selection = "${DBManager.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(contact.Id)
        db?.update(DBManager.TABLE_CONTACTS, values, selection, selectionArgs)
    }

    override fun remove(id: String) {
        val selection = "${DBManager.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id)
        db?.delete(DBManager.TABLE_CONTACTS, selection, selectionArgs)
    }

    override fun getAll(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = db?.query(
            DBManager.TABLE_CONTACTS,
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
                    Id = getString(getColumnIndexOrThrow(DBManager.COLUMN_ID))
                    Name = getString(getColumnIndexOrThrow(DBManager.COLUMN_NAME))
                    LastName = getString(getColumnIndexOrThrow(DBManager.COLUMN_LASTNAME))
                    Phone = getInt(getColumnIndexOrThrow(DBManager.COLUMN_PHONE))
                    Email = getString(getColumnIndexOrThrow(DBManager.COLUMN_EMAIL))
                    Address = getString(getColumnIndexOrThrow(DBManager.COLUMN_ADDRESS))
                    Country = getString(getColumnIndexOrThrow(DBManager.COLUMN_COUNTRY))
                    PhotoByteArray = getBlob(getColumnIndexOrThrow(DBManager.COLUMN_PHOTO))
                }
                contacts.add(contact)
            }
        }
        cursor?.close()
        return contacts
    }

    override fun getById(id: String): Contact? {
        val selection = "${DBManager.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id)
        val cursor = db?.query(
            DBManager.TABLE_CONTACTS,
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
                    Id = getString(getColumnIndexOrThrow(DBManager.COLUMN_ID))
                    Name = getString(getColumnIndexOrThrow(DBManager.COLUMN_NAME))
                    LastName = getString(getColumnIndexOrThrow(DBManager.COLUMN_LASTNAME))
                    Phone = getInt(getColumnIndexOrThrow(DBManager.COLUMN_PHONE))
                    Email = getString(getColumnIndexOrThrow(DBManager.COLUMN_EMAIL))
                    Address = getString(getColumnIndexOrThrow(DBManager.COLUMN_ADDRESS))
                    Country = getString(getColumnIndexOrThrow(DBManager.COLUMN_COUNTRY))
                    PhotoByteArray = getBlob(getColumnIndexOrThrow(DBManager.COLUMN_PHOTO))
                }
            }
        }
        cursor?.close()
        return contact
    }
}
