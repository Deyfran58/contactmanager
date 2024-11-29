package Database

import android.content.Context

class ContactDatabaseManager(context: Context) {
    private val dbHelper = ContactDatabaseHelper(context)

    // Agregar un registro de autenticación
    fun addAuthLog(status: String) {
        dbHelper.insertAuthLog(status)
    }

    // Obtener todos los registros de autenticación
    fun getAuthLogs(): List<String> {
        return dbHelper.getAuthLogs()
    }
}
