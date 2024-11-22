package data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import java.io.ByteArrayOutputStream

class ContactDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), IDBManager {

    companion object {
        const val DATABASE_NAME = "contact_database"
        const val DATABASE_VERSION = 1

        const val TABLE_CONTACT = "Contact"
        const val COLUMN_ID = "Id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_LASTNAME = "LastName"
        const val COLUMN_PHONE = "Phone"
        const val COLUMN_EMAIL = "Email"
        const val COLUMN_ADDRESS = "Address"
        const val COLUMN_COUNTRY = "Country"
        const val COLUMN_PHOTO = "Photo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CONTACT (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_LASTNAME TEXT NOT NULL,
                $COLUMN_PHONE INTEGER,
                $COLUMN_EMAIL TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_COUNTRY TEXT,
                $COLUMN_PHOTO BLOB
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACT")
        onCreate(db)
    }

    override fun add(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, contact.Id)
            put(COLUMN_NAME, contact.Name)
            put(COLUMN_LASTNAME, contact.LastName)
            put(COLUMN_PHONE, contact.Phone)
            put(COLUMN_EMAIL, contact.Email)
            put(COLUMN_ADDRESS, contact.Address)
            put(COLUMN_COUNTRY, contact.Country)
            put(COLUMN_PHOTO, contact.Photo?.let { bitmapToByteArray(it) })
        }
        db.insert(TABLE_CONTACT, null, values)
    }

    override fun update(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact.Name)
            put(COLUMN_LASTNAME, contact.LastName)
            put(COLUMN_PHONE, contact.Phone)
            put(COLUMN_EMAIL, contact.Email)
            put(COLUMN_ADDRESS, contact.Address)
            put(COLUMN_COUNTRY, contact.Country)
            put(COLUMN_PHOTO, contact.Photo?.let { bitmapToByteArray(it) })
        }
        db.update(TABLE_CONTACT, values, "$COLUMN_ID = ?", arrayOf(contact.Id))
    }

    override fun remove(id: String) {
        val db = writableDatabase
        db.delete(TABLE_CONTACT, "$COLUMN_ID = ?", arrayOf(id))
    }

    override fun getAll(): List<Contact> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTACT,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val contacts = mutableListOf<Contact>()
        with(cursor) {
            while (moveToNext()) {
                contacts.add(contactFromCursor(this))
            }
            close()
        }
        return contacts
    }

    override fun getById(id: String): Contact? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTACT,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val contact = contactFromCursor(cursor)
            cursor.close()
            contact
        } else {
            cursor.close()
            null
        }
    }

    private fun contactFromCursor(cursor: android.database.Cursor): Contact {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LASTNAME))
        val phone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
        val country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY))
        val photoBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO))
        val photoBitmap = photoBytes?.let { byteArrayToBitmap(it) }

        return Contact(id, name, lastName, phone, email, address, country, photoBitmap)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun byteArrayToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}