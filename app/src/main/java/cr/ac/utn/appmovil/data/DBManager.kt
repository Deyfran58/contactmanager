package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import java.io.ByteArrayOutputStream

class SQLiteDBManager(context: Context) : IDBManager {

    private val dbHelper = DBHelper(context)
    private val db: SQLiteDatabase = dbHelper.writableDatabase

    override fun add(contact: Contact) {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_ID, contact.Id)
            put(DBHelper.COLUMN_NAME, contact.Name)
            put(DBHelper.COLUMN_LASTNAME, contact.LastName)
            put(DBHelper.COLUMN_PHONE, contact.Phone)
            put(DBHelper.COLUMN_EMAIL, contact.Email)
            put(DBHelper.COLUMN_ADDRESS, contact.Address)
            put(DBHelper.COLUMN_COUNTRY, contact.Country)
            put(DBHelper.COLUMN_PHOTO, contact.Photo?.toByteArray())
        }
        db.insert(DBHelper.TABLE_CONTACTS, null, values)
    }

    override fun update(contact: Contact) {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_NAME, contact.Name)
            put(DBHelper.COLUMN_LASTNAME, contact.LastName)
            put(DBHelper.COLUMN_PHONE, contact.Phone)
            put(DBHelper.COLUMN_EMAIL, contact.Email)
            put(DBHelper.COLUMN_ADDRESS, contact.Address)
            put(DBHelper.COLUMN_COUNTRY, contact.Country)
            put(DBHelper.COLUMN_PHOTO, contact.Photo?.toByteArray())
        }
        db.update(DBHelper.TABLE_CONTACTS, values, "${DBHelper.COLUMN_ID} = ?", arrayOf(contact.Id  ))
    }

    override fun remove(id: String) {
        db.delete(DBHelper.TABLE_CONTACTS, "${DBHelper.COLUMN_ID} = ?", arrayOf(id))
    }

    override fun getAll(): List<Contact> {
        val contacts = mutableListOf<Contact>()
        val cursor = db.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                val contact = cursorToContact(cursor)
                contacts.add(contact)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contacts
    }

    override fun getById(id: String): Contact? {
        val cursor = db.query(DBHelper.TABLE_CONTACTS, null, "${DBHelper.COLUMN_ID} = ?", arrayOf(id), null, null, null)
        cursor?.moveToFirst()?.let {
            val contact = cursorToContact(cursor)
            cursor.close()
            return contact
        }
        cursor?.close()
        return null
    }

    private fun cursorToContact(cursor: Cursor): Contact {
        return Contact(
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_LASTNAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PHONE)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ADDRESS)),
            cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_COUNTRY)),
            cursor.getBlob(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PHOTO))?.toBitmap())
    }
    fun Bitmap.toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        this.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    fun ByteArray?.toBitmap(): Bitmap? {
        return this?.let {
            BitmapFactory.decodeByteArray(it, 0, this.size)
        }
    }
}
