package cr.ac.utn.appmovil.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import java.io.ByteArrayOutputStream

class ContactDbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contacts.db"
        const val DATABASE_VERSION = 1

        const val TABLE_CONTACTS = "contacts"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LAST_NAME = "last_name"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_PHOTO = "photo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_CONTACTS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_LAST_NAME TEXT,
                $COLUMN_PHONE INTEGER,
                $COLUMN_EMAIL TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_COUNTRY TEXT,
                $COLUMN_PHOTO BLOB
            )
        """
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }

    fun convertBitmapToByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    fun convertByteArrayToBitmap(byteArray: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    private fun cursorToContact(cursor: Cursor): Contact {
        return Contact(
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LAST_NAME)),
            cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS)),
            cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)),
            convertByteArrayToBitmap(cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO)))
        )
    }

    fun insertContact(contact: Contact) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, contact.Id)
            put(COLUMN_NAME, contact.Name)
            put(COLUMN_LAST_NAME, contact.LastName)
            put(COLUMN_PHONE, contact.Phone)
            put(COLUMN_EMAIL, contact.Email)
            put(COLUMN_ADDRESS, contact.Address)
            put(COLUMN_COUNTRY, contact.Country)
            put(COLUMN_PHOTO, convertBitmapToByteArray(contact.Photo))
        }
        db.insert(TABLE_CONTACTS, null, values)
        db.close()
    }


    fun getAllContacts(): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        val db = readableDatabase
        var cursor: Cursor? = null
        return try {
            cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, null)
            while (cursor.moveToNext()) {
                contactsList.add(cursorToContact(cursor))
            }
            contactsList
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            cursor?.close()
            db.close()
        }
    }

    fun getContactById(id: String): Contact? {
        val db = readableDatabase
        var cursor: Cursor? = null
        return try {
            cursor = db.query(
                TABLE_CONTACTS,
                null,
                "$COLUMN_ID = ?",
                arrayOf(id),
                null,
                null,
                null
            )
            if (cursor.moveToFirst()) {
                cursorToContact(cursor)
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            cursor?.close()
            db.close()
        }
    }
}

