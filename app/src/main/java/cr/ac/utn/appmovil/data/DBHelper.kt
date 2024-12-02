package cr.ac.utn.appmovil.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1

        // Table Name
        const val TABLE_CONTACTS = "contacts"
        const val TABLE_CONTACT_PHOTOS = "ContactPhotos"

        // Columns
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_LASTNAME = "lastname"
        const val COLUMN_PHONE = "phone"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_COUNTRY = "country"
        const val COLUMN_PHOTO = "photo"

        // Create Table Statements
        private const val CREATE_TABLE_CONTACTS = """
            CREATE TABLE $TABLE_CONTACTS (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_LASTNAME TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_COUNTRY TEXT
            )
        """

        private const val CREATE_TABLE_CONTACT_PHOTOS = """
            CREATE TABLE $TABLE_CONTACT_PHOTOS (
                photo_id INTEGER PRIMARY KEY AUTOINCREMENT,
                contact_id TEXT,
                photo_part BLOB,
                part_index INTEGER,
                FOREIGN KEY(contact_id) REFERENCES $TABLE_CONTACTS($COLUMN_ID)
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_CONTACTS)
        db.execSQL(CREATE_TABLE_CONTACT_PHOTOS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACT_PHOTOS")
        onCreate(db)
    }
}
