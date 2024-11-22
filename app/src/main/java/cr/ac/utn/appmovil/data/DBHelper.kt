package cr.ac.utn.appmovil.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "contacts.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_CONTACTS = "contacts"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_LASTNAME = "lastname"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_COUNTRY = "country"
        private const val COLUMN_PHOTO = "photo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_CONTACTS (" +
                "$COLUMN_ID TEXT PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_LASTNAME TEXT," +
                "$COLUMN_PHONE INTEGER," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_ADDRESS TEXT," +
                "$COLUMN_COUNTRY TEXT," +
                "$COLUMN_PHOTO BLOB)")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CONTACTS")
        onCreate(db)
    }
}
