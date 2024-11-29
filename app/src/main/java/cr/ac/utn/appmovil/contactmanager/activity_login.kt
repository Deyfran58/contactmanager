package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.google.android.ads.mediationtestsuite.activities.HomeActivity
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Referencias a los elementos del layout
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerTextView: TextView = findViewById(R.id.registerTextView)

        // Configurar acción del botón de iniciar sesión
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                authenticateUser(username, password)
            }
        }

        // Configurar acción del texto de registro
        registerTextView.setOnClickListener {
            Toast.makeText(this, "Redirigiendo a la pantalla de registro...", Toast.LENGTH_SHORT).show()
            // Agrega la lógica para redirigir a la pantalla de registro
        }
    }

    private fun authenticateUser(username: String, password: String) {
        val url = "https://apicontainers.azurewebsites.net/technicians/validateAuth"
        val client = OkHttpClient()

        // Crear JSON para enviar en el cuerpo del POST
        val json = JSONObject()
        json.put("username", username)
        json.put("password", password)

        val requestBody = RequestBody.create(MediaType.parse("application/json"), json.toString())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        // Realizar la llamada al API
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Error en la conexión: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                Log.e("LoginActivity", "Error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body()?.string()
                if (response.isSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Autenticación exitosa", Toast.LENGTH_SHORT).show()
                        // Redirigir a otra actividad, por ejemplo, HomeActivity
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    val errorMessage = JSONObject(responseBody ?: "{}").optString("error", "Autenticación fallida")
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


}