package cr.ac.utn.appmovil.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AuthDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "authentications.db"
        private const val DATABASE_VERSION = 1

        // Table Name
        const val TABLE_AUTH = "Authentications"

        // Columns
        const val COLUMN_ID = "auth_id"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_TIMESTAMP = "timestamp"

        // Create Table Statement
        private const val CREATE_TABLE_AUTH = """
            CREATE TABLE $TABLE_AUTH (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_AUTH)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AUTH")
        onCreate(db)
    }

    fun saveAuthentication(email: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
        }
        db.insert(TABLE_AUTH, null, values)
    }

    fun getAllAuthentications(): List<Authentication> {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_AUTH, null, null, null, null, null, "$COLUMN_TIMESTAMP DESC")
        val authList = mutableListOf<Authentication>()

        if (cursor.moveToFirst()) {
            do {
                val auth = Authentication(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                )
                authList.add(auth)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return authList
    }
    data class Authentication(val id: Int, val email: String, val timestamp: String)
}