package cr.ac.utn.appmovil.contactmanager
import Internet.retroCliente
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import data.SQlManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.identities.LoginEvent
import data.loginRes


class Register_person : AppCompatActivity() {

    private lateinit var usName: EditText
    private lateinit var uspass: EditText
    private lateinit var usLogBtn: Button

    private lateinit var dbManager: SQlManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_person)

        usName = findViewById(R.id.txtUsid)
        uspass = findViewById(R.id.txtUsPassword)
        usLogBtn = findViewById(R.id.btn_login_us)

        dbManager = SQlManager(this)

        usLogBtn.setOnClickListener {
            realizarLogin()
        }
    }

    private fun realizarLogin() {
        val username = usName.text.toString().trim()
        val password = uspass.text.toString().trim()

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
                                this@Register_person,
                                getString(
                                    R.string.Welc,

                                ),
                                Toast.LENGTH_SHORT
                            ).show()

                            registrarEventoLogin(username)

                            GoMain()
                        } else {
                            Toast.makeText(
                                this@Register_person,
                                loginResponse?.message ?: getString(R.string.Badreq),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            this@Register_person,
                            getString(R.string.Badreq),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<loginRes>, t: Throwable) {
                    Toast.makeText(
                        this@Register_person,
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

    private fun GoMain() {
        val intent = Intent(this@Register_person, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}