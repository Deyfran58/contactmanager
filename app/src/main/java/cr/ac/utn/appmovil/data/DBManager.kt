package cr.ac.utn.appmovil.data
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.interfaces.IDBManager
import cr.ac.utn.appmovil.util.util

class DBManager(private val dbhelperlite: ContactDataBaseHelper) : IDBManager {


    override fun add(contact: Contact) {
        val db = dbhelperlite.writableDatabase
        val values = ContentValues().apply {
            put(ContactDataBaseHelper.COLUMN_ID, contact.Id)
            put(ContactDataBaseHelper.COLUMN_NAME, contact.Name)
            put(ContactDataBaseHelper.COLUMN_LASTNAME, contact.LastName)
            put(ContactDataBaseHelper.COLUMN_PHONE, contact.Phone)
            put(ContactDataBaseHelper.COLUMN_EMAIL, contact.Email)
            put(ContactDataBaseHelper.COLUMN_ADDRESS, contact.Address)
            put(ContactDataBaseHelper.COLUMN_COUNTRY, contact.Country)
            put(ContactDataBaseHelper.COLUMN_PHOTO, util.convertToByteArray(contact.Photo))
        }
        try {
            db.insert(ContactDataBaseHelper.TABLE_CONT, null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    override fun getAll(): List<Contact> {
        val db = dbhelperlite.readableDatabase
        val cursor: Cursor = db.query(ContactDataBaseHelper.TABLE_CONT, null, null, null, null, null, null)
        val contacts = mutableListOf<Contact>()
        with(cursor) {
            while (moveToNext()) {
                val photoBlob = getBlob(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_PHOTO))
                val photo = photoBlob?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

                val contact = Contact(
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_ID)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_NAME)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_LASTNAME)),
                    getInt(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_PHONE)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_EMAIL)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_ADDRESS)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_COUNTRY)),
                    photo ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                )
                contacts.add(contact)
            }
        }
        cursor.close()
        db.close()
        return contacts
    }

    override fun getById(id: String): Contact? {
        val db = dbhelperlite.readableDatabase
        val cursor: Cursor = db.query(ContactDataBaseHelper.TABLE_CONT, null, "id = ?", arrayOf(id), null, null, null)
        var contact: Contact? = null
        with(cursor) {
            if (moveToFirst()) {
                val photoBlob = getBlob(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_PHOTO))
                val photo = photoBlob?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

                contact = Contact(
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_ID)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_NAME)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_LASTNAME)),
                    getInt(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_PHONE)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_EMAIL)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_ADDRESS)),
                    getString(getColumnIndexOrThrow(ContactDataBaseHelper.COLUMN_COUNTRY)),
                    photo ?: Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
                )
            }
        }
        cursor.close()
        db.close()
        return contact
    }


    override fun update(contact: Contact) {
        val db = dbhelperlite.writableDatabase
        val values = ContentValues().apply {
            put(ContactDataBaseHelper.COLUMN_NAME, contact.Name)
            put(ContactDataBaseHelper.COLUMN_LASTNAME, contact.LastName)
            put(ContactDataBaseHelper.COLUMN_PHONE, contact.Phone)
            put(ContactDataBaseHelper.COLUMN_EMAIL, contact.Email)
            put(ContactDataBaseHelper.COLUMN_ADDRESS, contact.Address)
            put(ContactDataBaseHelper.COLUMN_COUNTRY, contact.Country)
            put(ContactDataBaseHelper.COLUMN_PHOTO, util.convertToByteArray(contact.Photo))
        }
        try {
            db.update(ContactDataBaseHelper.TABLE_CONT, values, "${ContactDataBaseHelper.COLUMN_ID} = ?", arrayOf(contact.Id))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    override fun remove(id: String) {
        TODO("Not yet implemented")
    }


    override fun delete(id: String) {
        val db = dbhelperlite.writableDatabase
        try {
            db.delete(ContactDataBaseHelper.TABLE_CONT, "${ContactDataBaseHelper.COLUMN_ID} = ?", arrayOf(id))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }
}
