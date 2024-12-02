package cr.ac.utn.appmovil.contactmanager

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cr.ac.utn.appmovil.data.AuthDBHelper
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class logueo : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var dbHelper: AuthDBHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_logueo)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        dbHelper = AuthDBHelper(this)
        btnLogin.setOnClickListener {
            val id = etEmail.text.toString()
            val password = etPassword.text.toString()
            authenticateUser(id, password) }
    }
    private fun authenticateUser(id: String, password: String) {
        val url = "https://apicontainers.azurewebsites.net/technicians/validateAuth"

        val client = OkHttpClient()
        val json = JSONObject().apply {
            put("id", id)
            put("password", password)
        }
        val body = RequestBody.create("application/json; charset=utf-8".toMediaType(), json.toString())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@logueo, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val jsonResponse = JSONObject(responseBody)
                    val responseCode = jsonResponse.getInt("responseCode")

                    if (responseCode == 0) {
                        val data = jsonResponse.getJSONObject("data")
                        val userId = data.getString("id")
                        val userName = data.getString("name")
                        val userLastName = data.getString("lastName")
                        val isActive = data.getBoolean("isActive")

                        runOnUiThread {
                            if (isActive) {
                                Toast.makeText(this@logueo, "Welcome, $userName $userLastName!", Toast.LENGTH_SHORT).show()
                                dbHelper.saveAuthentication(userId)
                                startActivity(Intent(this@logueo, MainActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this@logueo, "Account is not active", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@logueo, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@logueo, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}