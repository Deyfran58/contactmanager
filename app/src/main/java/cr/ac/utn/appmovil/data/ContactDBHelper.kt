package cr.ac.utn.appmovil.data


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import java.io.ByteArrayOutputStream

class ContactDBHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "contacts_db"
        const val DB_VERSION = 1

        const val TABLE_NAME = "Contacts"
        const val COL_ID = "ContactId"
        const val COL_FIRST_NAME = "FirstName"
        const val COL_LAST_NAME = "LastName"
        const val COL_PHONE_NUMBER = "PhoneNumber"
        const val COL_EMAIL_ADDRESS = "EmailAddress"
        const val COL_RESIDENCE = "Residence"
        const val COL_NATIONALITY = "Nationality"
        const val COL_IMAGE = "ProfileImage"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COL_ID TEXT PRIMARY KEY,
                $COL_FIRST_NAME TEXT NOT NULL,
                $COL_LAST_NAME TEXT NOT NULL,
                $COL_PHONE_NUMBER INTEGER,
                $COL_EMAIL_ADDRESS TEXT,
                $COL_RESIDENCE TEXT,
                $COL_NATIONALITY TEXT,
                $COL_IMAGE BLOB
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addContact(contact: Contact): Long {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COL_ID, contact.Id)
                put(COL_FIRST_NAME, contact.Name)
                put(COL_LAST_NAME, contact.LastName)
                put(COL_PHONE_NUMBER, contact.Phone)
                put(COL_EMAIL_ADDRESS, contact.Email)
                put(COL_RESIDENCE, contact.Address)
                put(COL_NATIONALITY, contact.Country)
                put(COL_IMAGE, contact.Photo?.let { convertBitmapToBytes(it) })
            }
            db.insert(TABLE_NAME, null, values)
        } finally {
            db.close()
        }
    }

    fun modifyContact(contact: Contact): Int {
        val db = writableDatabase
        return try {
            val values = ContentValues().apply {
                put(COL_FIRST_NAME, contact.Name)
                put(COL_LAST_NAME, contact.LastName)
                put(COL_PHONE_NUMBER, contact.Phone)
                put(COL_EMAIL_ADDRESS, contact.Email)
                put(COL_RESIDENCE, contact.Address)
                put(COL_NATIONALITY, contact.Country)
                put(COL_IMAGE, contact.Photo?.let { convertBitmapToBytes(it) })
            }
            db.update(TABLE_NAME, values, "$COL_ID = ?", arrayOf(contact.Id))
        } finally {
            db.close()
        }
    }

    fun removeContactById(contactId: String): Int {
        val db = writableDatabase
        return try {
            db.delete(TABLE_NAME, "$COL_ID = ?", arrayOf(contactId))
        } finally {
            db.close()
        }
    }

    fun fetchContactById(contactId: String): Contact? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, "$COL_ID = ?", arrayOf(contactId),
            null, null, null
        )
        return try {
            if (cursor.moveToFirst()) extractContactFromCursor(cursor) else null
        } finally {
            cursor.close()
            db.close()
        }
    }

    fun fetchAllContacts(): List<Contact> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME, null, null, null,
            null, null, null
        )
        val contacts = mutableListOf<Contact>()
        try {
            while (cursor.moveToNext()) {
                contacts.add(extractContactFromCursor(cursor))
            }
        } finally {
            cursor.close()
            db.close()
        }
        return contacts
    }

    private fun extractContactFromCursor(cursor: android.database.Cursor): Contact {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(COL_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_FIRST_NAME))
        val lastName = cursor.getString(cursor.getColumnIndexOrThrow(COL_LAST_NAME))
        val phone = cursor.getInt(cursor.getColumnIndexOrThrow(COL_PHONE_NUMBER))
        val email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL_ADDRESS))
        val residence = cursor.getString(cursor.getColumnIndexOrThrow(COL_RESIDENCE))
        val nationality = cursor.getString(cursor.getColumnIndexOrThrow(COL_NATIONALITY))
        val imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COL_IMAGE))
        val imageBitmap = imageBytes?.let { convertBytesToBitmap(it) }

        return Contact(id, name, lastName, phone, email, residence, nationality, imageBitmap)
    }

    private fun convertBitmapToBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun convertBytesToBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}