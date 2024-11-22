package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import java.io.ByteArrayOutputStream

class DBhelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contacto"
        const val DATABASE_VERSION = 1

        const val TABLE_CONTS = "Contactos"
        const val COLUMN_ID = "Id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_LASTNAME = "LastName"
        const val COLUMN_Phone = "Phone"
        const val COLUMN_EMAIL = "Email"
        const val COLUMN_COUNTRY = "Country"
        const val COLUMN_ADDRES = "Adress"
        const val COLUMN_PHOTO = "Photo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_CONTS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_LASTNAME TEXT NOT NULL,
                $COLUMN_Phone INTEGER NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_COUNTRY TEXT NOT NULL,
                $COLUMN_ADDRES TEXT NOT NULL,
                $COLUMN_PHOTO BLOB
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTS")
        onCreate(db)
    }

    fun insertCotact(contact: Contact): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, contact.Id)
            put(COLUMN_NAME, contact.Name)
            put(COLUMN_LASTNAME, contact.LastName)
            put(COLUMN_Phone, contact.Phone)
            put(COLUMN_EMAIL, contact.Email)
            put(COLUMN_COUNTRY, contact.Country)
            put(COLUMN_ADDRES, contact.Address)
            put(COLUMN_PHOTO, contact.Photo?.let { bitmapToByteArray(it) })
        }
        return db.insert(TABLE_CONTS, null, values)
    }

    fun updateCotact(contact: Contact): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ID, contact.Id)
            put(COLUMN_NAME, contact.Name)
            put(COLUMN_LASTNAME, contact.LastName)
            put(COLUMN_Phone, contact.Phone)
            put(COLUMN_EMAIL, contact.Email)
            put(COLUMN_COUNTRY, contact.Country)
            put(COLUMN_ADDRES, contact.Address)
            put(COLUMN_PHOTO, contact.Photo?.let { bitmapToByteArray(it) })
        }
        return db.update(TABLE_CONTS, values, "$COLUMN_ID = ?", arrayOf(contact.Id))
    }

    fun deleteCotactById(id: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_CONTS, "$COLUMN_ID = ?", arrayOf(id))
    }

    fun getCotactById(id: String): Contact? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id),
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val cont = personFromCursor(cursor)
            cursor.close()
            cont
        } else {
            cursor.close()
            null
        }
    }

    fun getAllCotact(): List<Contact> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_CONTS,
            null,
            null,
            null,
            null,
            null,
            null
        )
        val contactList = mutableListOf<Contact>()
        with(cursor) {
            while (moveToNext()) {
                contactList.add(personFromCursor(this))
            }
            close()
        }
        return contactList
    }



    private fun personFromCursor(cursor: android.database.Cursor): Contact {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val LastName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LASTNAME))
        val Phone = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_Phone))
        val Email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL))
        val Conuntry = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY))
        val Adress = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRES))
        val photoBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO))
        val photoBitmap = photoBytes?.let { byteArrayToBitmap(it) }

        return Contact(id, name, LastName, Phone, Email,Conuntry,Adress,photoBitmap)
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