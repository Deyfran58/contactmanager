package cr.ac.utn.appmovil.contactmanager

import Internet.retroCliente
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.identities.LoginEvent

import data.SQlManager
import data.loginRes
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Login : AppCompatActivity() {
    private lateinit var dbManager: SQlManager
    private lateinit var ID: EditText
    private lateinit var pass: EditText
    private lateinit var log: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ID = findViewById(R.id.txt_id)
        pass = findViewById(R.id.txt_password)
        log = findViewById(R.id.btn_log)

        dbManager = SQlManager(this)

        log.setOnClickListener {
            Log()
        }
    }

    private fun Log() {
        val username = ID.text.toString().trim()
        val password = pass.text.toString().trim()

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.missingSpace), Toast.LENGTH_SHORT).show()
            return
        }

        val credentials = mapOf("id" to username, "password" to password)

        retroCliente.authInstance.validateAuth(credentials)
            .enqueue(object : Callback<loginRes> {
                override fun onResponse(call: Call<loginRes>, response: Response<loginRes>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse?.responseCode == 0) {
                            Toast.makeText(
                                this@Login,
                                getString(
                                    R.string.Welc,

                                ),
                                Toast.LENGTH_SHORT
                            ).show()

                            registrarEventoLogin(username)

                            navegarAMainActivity()
                        } else {
                            Toast.makeText(
                                this@Login,
                                loginResponse?.message ?: getString(R.string.Badreq),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@Login,
                            getString(R.string.Badreq),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<loginRes>, t: Throwable) {
                    Toast.makeText(
                        this@Login,
                        getString(R.string.Badreq),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    private fun registrarEventoLogin(username: String) {
        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val loginEvent = LoginEvent(
            userId = username,
            loginTime = currentTime
        )
        dbManager.addLoginEvent(loginEvent)
    }

    private fun navegarAMainActivity() {
        val intent = Intent(this@Login, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}