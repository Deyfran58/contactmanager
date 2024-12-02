package cr.ac.utn.appmovil.contactmanager

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.data.AuthDBHelper

class listaAuthenticate : AppCompatActivity() {

    private lateinit var listViewAuthentications: ListView
    private lateinit var dbHelper: AuthDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_authenticate)

        listViewAuthentications = findViewById(R.id.listViewAuthentications)
        dbHelper = AuthDBHelper(this)

        displayAuthentications()
    }

    private fun displayAuthentications() {
        val authList = dbHelper.getAllAuthentications()
        val authStrings = authList.map { "ID: ${it.id}, Email: ${it.email}, Timestamp: ${it.timestamp}" }

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, authStrings)
        listViewAuthentications.adapter = adapter
    }
}
