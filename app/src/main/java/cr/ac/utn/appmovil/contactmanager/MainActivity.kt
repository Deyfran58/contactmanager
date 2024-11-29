package cr.ac.utn.appmovil.contactmanager

import Database.ContactDatabaseManager
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStream
import android.util.Log
import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private lateinit var dbManager: ContactDatabaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbManager = ContactDatabaseManager(this)

        // Botón: Mostrar logs de autenticación
        val btnAuthLogs: Button = findViewById(R.id.main_btnAuthLogs)
        btnAuthLogs.setOnClickListener {
            showAuthLogs()
        }

        // Botón: Manejar autenticación
        val btnAuthenticate: Button = findViewById(R.id.main_btnAuthenticate)
        btnAuthenticate.setOnClickListener {
            handleAuthentication()
        }

        // Botón: Redirigir a la lista de contactos
        val btnContactList: Button = findViewById(R.id.main_btnContactList)
        btnContactList.setOnClickListener {
            startActivity(Intent(this, ContactListActivity::class.java))
        }

        // Botón: Redirigir a la lista personalizada de contactos
        val btnContactListCustom: Button = findViewById(R.id.main_btnContactListCustom)
        btnContactListCustom.setOnClickListener {
            startActivity(Intent(this, ContactListCustomActivity::class.java))
        }

        // Botón: Redirigir a la actividad de agregar contacto
        val btnAddContact: Button = findViewById(R.id.main_btnAddContact)
        btnAddContact.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }
    }

    // Función para manejar la autenticación
    private fun authenticate(username: String, password: String): String {
        val url = URL("https://apicontainers.azurewebsites.net/technicians/validateAuth")
        val payload = JSONObject()
        payload.put("username", username)
        payload.put("password", password)

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.doOutput = true

        return try {
            val outputStream: OutputStream = connection.outputStream
            outputStream.write(payload.toString().toByteArray(Charsets.UTF_8))
            outputStream.close()

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                "Success"
            } else {
                "Failed"
            }
        } catch (e: Exception) {
            Log.e("AuthAPI", e.message.toString())
            "Error"
        } finally {
            connection.disconnect()
        }
    }

    // Función para manejar el proceso de autenticación
    private fun handleAuthentication() {
        val username = "user_input" // Reemplazar con la entrada real del usuario
        val password = "password_input" // Reemplazar con la entrada real del usuario

        val result = authenticate(username, password)
        dbManager.addAuthLog(result)
        Toast.makeText(this, "Authentication: $result", Toast.LENGTH_LONG).show()
    }

    // Función para mostrar los logs de autenticación
    private fun showAuthLogs() {
        val logs = dbManager.getAuthLogs()
        val logString = logs.joinToString("\n")
        AlertDialog.Builder(this)
            .setTitle("Authentication Logs")
            .setMessage(logString)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
