package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import java.io.ByteArrayOutputStream


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION), IDBManager {

    companion object {
        const val DATABASE_NAME = "contacts.db"
        const val DATABASE_VERSION = 1
        const val TABLE_CONTACT = "Contact"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LASTNAME = "lastname"
        const val COLUMN_PHONE= "phone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_IMAGE = "image"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CONTACT (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_LASTNAME TEXT NOT NULL,
                $COLUMN_PHONE TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_ADDRESS TEXT NOT NULL,
                $COLUMN_COUNTRY TEXT NOT NULL,
                $COLUMN_IMAGE BLOB
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACT")
        onCreate(db)
    }



    // Métodos CRUD
    override fun addContact(contact: Contact ) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact._name)
            put(COLUMN_LASTNAME, contact._lastName)
            put(COLUMN_PHONE, contact._phone)
            put(COLUMN_EMAIL, contact._email)
            put(COLUMN_ADDRESS, contact._address)
            put(COLUMN_COUNTRY, contact._country)
            put(COLUMN_IMAGE, contact._photo?.let { bitmapToByteArray(it) })
        }
        db.insert(TABLE_CONTACT, null, values)
        db.close()
    }

    override fun updateContact(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, contact._name)
            put(COLUMN_LASTNAME, contact._lastName)
            put(COLUMN_PHONE, contact._phone)
            put(COLUMN_EMAIL, contact._email)
            put(COLUMN_ADDRESS, contact._address)
            put(COLUMN_COUNTRY, contact._country)
            put(COLUMN_IMAGE, contact._photo?.let { bitmapToByteArray(it) })
        }
        db.update(TABLE_CONTACT, values, "$COLUMN_ID = ?", arrayOf(contact._id.toString()))
        db.close()
    }

    override fun removeContact(id: String) {
        val db = writableDatabase
        db.delete(TABLE_CONTACT, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    override fun getAllContact(): List<Contact> {
        val db = readableDatabase
        val services = mutableListOf<Contact>()
        val cursor = db.query(TABLE_CONTACT, null, null, null, null, null, null)
        if (cursor.moveToFirst()) {
            do {
                services.add(cursorToContact(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return services
    }

    override fun getContactById(id: String): Contact? {
        val db = readableDatabase
        val cursor = db.query(TABLE_CONTACT, null, "$COLUMN_ID = ?", arrayOf(id.toString()), null, null, null)
        if (cursor.moveToFirst()) {
            val contact = cursorToContact(cursor)
            cursor.close()
            db.close()
            return contact
        }
        cursor.close()
        db.close()
        return null
    }

    override fun getContactByName(name: String): Contact? {
        val db = readableDatabase
        val cursor = db.query( TABLE_CONTACT, null, "$COLUMN_NAME = ?", arrayOf(name), null, null, null)
        if (cursor.moveToFirst()) {
            val contact = cursorToContact(cursor)
            cursor.close()
            db.close()
            return contact
        }
        cursor.close()
        db.close()
        return null
    }

    // Métodos auxiliares
    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    private fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun cursorToContact(cursor: android.database.Cursor): Contact {
        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val lastname = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LASTNAME))
        val phone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
        val country = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY))
        val image = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))
        val bitmap = image?.let { byteArrayToBitmap(it) }
        return Contact(id.toString(), name, lastname, phone, email, address, country, bitmap)
    }
}