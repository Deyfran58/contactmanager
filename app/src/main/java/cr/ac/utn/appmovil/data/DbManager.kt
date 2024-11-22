package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory

import java.io.ByteArrayOutputStream
fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}
class DbManager(context: Context) : IDBManager {

    private val dbHelper = DbHelper(context)

    override fun add(contact: Contact) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DbHelper.COLUMN_ID, contact.Id)
            put(DbHelper.COLUMN_NAME, contact.Name)
            put(DbHelper.COLUMN_LASTNAME, contact.LastName)
            put(DbHelper.COLUMN_PHONE, contact.Phone)
            put(DbHelper.COLUMN_EMAIL, contact.Email)
            put(DbHelper.COLUMN_ADDRESS, contact.Address)
            put(DbHelper.COLUMN_COUNTRY, contact.Country)
            put(DbHelper.COLUMN_PHOTO, contact.Photo.toByteArray())
        }
        db.insert(DbHelper.TABLE_CONTACTS, null, values)
        db.close()
    }

    override fun update(contact: Contact) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DbHelper.COLUMN_NAME, contact.Name)
            put(DbHelper.COLUMN_LASTNAME, contact.LastName)
            put(DbHelper.COLUMN_PHONE, contact.Phone)
            put(DbHelper.COLUMN_EMAIL, contact.Email)
            put(DbHelper.COLUMN_ADDRESS, contact.Address)
            put(DbHelper.COLUMN_COUNTRY, contact.Country)
            put(DbHelper.COLUMN_PHOTO, contact.Photo.toByteArray())
        }
        db.update(DbHelper.TABLE_CONTACTS, values, "${DbHelper.COLUMN_ID} = ?", arrayOf(contact.Id))
        db.close()
    }

    override fun remove(id: String) {
        val db = dbHelper.writableDatabase
        db.delete(DbHelper.TABLE_CONTACTS, "${DbHelper.COLUMN_ID} = ?", arrayOf(id))
        db.close()
    }

    override fun getAll(): List<Contact> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(DbHelper.TABLE_CONTACTS, null, null, null, null, null, null)
        val contacts = mutableListOf<Contact>()
        if (cursor.moveToFirst()) {
            do {
                contacts.add(cursor.toContact())
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return contacts
    }

    override fun getById(id: String): Contact? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DbHelper.TABLE_CONTACTS,
            null,
            "${DbHelper.COLUMN_ID} = ?",
            arrayOf(id),
            null,
            null,
            null
        )
        val contact = if (cursor.moveToFirst()) cursor.toContact() else null
        cursor.close()
        db.close()
        return contact
    }
}

private fun Cursor.toContact(): Contact {
    return Contact(
        id = getString(getColumnIndexOrThrow(DbHelper.COLUMN_ID)),
        name = getString(getColumnIndexOrThrow(DbHelper.COLUMN_NAME)),
        lastName = getString(getColumnIndexOrThrow(DbHelper.COLUMN_LASTNAME)),
        phone = getInt(getColumnIndexOrThrow(DbHelper.COLUMN_PHONE)),
        email = getString(getColumnIndexOrThrow(DbHelper.COLUMN_EMAIL)),
        address = getString(getColumnIndexOrThrow(DbHelper.COLUMN_ADDRESS)),
        country = getString(getColumnIndexOrThrow(DbHelper.COLUMN_COUNTRY)),
        photo = getBlob(getColumnIndexOrThrow(DbHelper.COLUMN_PHOTO)).toBitmap()
    )
}
