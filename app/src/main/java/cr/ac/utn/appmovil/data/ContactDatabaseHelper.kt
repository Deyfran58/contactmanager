package cr.ac.utn.appmovil.data


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class ContactDatabaseHelper (context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "Contact"
        const val DATABASE_VERSION = 1
        const val TABLE_CONT = "Contact"
        const val COLUMN_ID = "Id"
        const val COLUMN_NAME = "Name"
        const val COLUMN_LASTNAME = "LastName"
        const val COLUMN_Phone = "Phone"
        const val COLUMN_EMAIL = "Email"
        const val COLUMN_COUNTRY = "Country"
        const val COLUMN_ADDRES = "Adress"
        const val COLUMN_PHOTO = "Photo"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_CONT (" +
                "$COLUMN_ID TEXT PRIMARY KEY," +
                "$COLUMN_NAME TEXT," +
                "$COLUMN_LASTNAME TEXT," +
                "$COLUMN_Phone INTEGER," +
                "$COLUMN_EMAIL TEXT," +
                "$COLUMN_ADDRES TEXT," +
                "$COLUMN_COUNTRY TEXT," +
                "$COLUMN_PHOTO BLOB)")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONT")
        onCreate(db)
    }



}