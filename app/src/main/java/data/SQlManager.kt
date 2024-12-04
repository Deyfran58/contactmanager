package data
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import cr.ac.utn.appmovil.data.DBHelper
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.identities.LoginEvent
import cr.ac.utn.appmovil.interfaces.IDBManager

class SQlManager (context: Context): IDBManager {

    private val dbHelper: DBHelper = DBHelper(context)
    private var db: SQLiteDatabase? = null

    init {
        db = dbHelper.writableDatabase
    }


    fun addLoginEvent(loginEvent: LoginEvent) {
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_USER_ID, loginEvent.userId)
            put(DBHelper.COLUMN_LOGIN_TIME, loginEvent.loginTime)
        }
        db?.insert(DBHelper.TABLE_LOGIN_EVENTS, null, values)
    }
    fun getAllLoginEvents(): List<LoginEvent> {
        val loginEvents = mutableListOf<LoginEvent>()
        val cursor = db?.query(
            DBHelper.TABLE_LOGIN_EVENTS,
            null,
            null,
            null,
            null,
            null,
            "${DBHelper.COLUMN_EVENT_ID} DESC" // Ordenar por ID descendente (últimos primero)
        )
        with(cursor) {
            while (this != null && moveToNext()) {
                val loginEvent = LoginEvent(
                    eventId = getInt(getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_ID)),
                    userId = getString(getColumnIndexOrThrow(DBHelper.COLUMN_USER_ID)),
                    loginTime = getString(getColumnIndexOrThrow(DBHelper.COLUMN_LOGIN_TIME))
                )
                loginEvents.add(loginEvent)
            }
        }
        cursor?.close()
        return loginEvents
    }

    override fun add(contact: Contact) {
        TODO("Not yet implemented")
    }

    override fun update(contact: Contact) {
        TODO("Not yet implemented")
    }

    override fun remove(id: String) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Contact> {
        TODO("Not yet implemented")
    }

    override fun getById(id: String): Contact? {
        TODO("Not yet implemented")
    }

}