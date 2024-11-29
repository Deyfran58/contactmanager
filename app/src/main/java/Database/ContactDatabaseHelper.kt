package Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ContactDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contact_database"
        const val DATABASE_VERSION = 2

        // Tabla para registros de autenticación
        const val TABLE_AUTH_LOG = "AuthLog"
        const val COLUMN_AUTH_ID = "Id"
        const val COLUMN_TIMESTAMP = "Timestamp"
        const val COLUMN_STATUS = "Status"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla para almacenar logs de autenticación
        val createAuthLogTable = """
            CREATE TABLE $TABLE_AUTH_LOG (
                $COLUMN_AUTH_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP,
                $COLUMN_STATUS TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createAuthLogTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_AUTH_LOG")
        onCreate(db)
    }

    // Insertar un registro de autenticación
    fun insertAuthLog(status: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_STATUS, status)
        }
        return db.insert(TABLE_AUTH_LOG, null, values)
    }

    // Obtener todos los registros de autenticación
    fun getAuthLogs(): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_AUTH_LOG,
            null,
            null,
            null,
            null,
            null,
            "$COLUMN_TIMESTAMP DESC"
        )
        val logs = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val timestamp = getString(getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                val status = getString(getColumnIndexOrThrow(COLUMN_STATUS))
                logs.add("$timestamp - $status")
            }
            close()
        }
        return logs
    }
}
