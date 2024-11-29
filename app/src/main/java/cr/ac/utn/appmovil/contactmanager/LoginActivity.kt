package cr.ac.utn.appmovil.contactmanager

import ApiService
import TechnicianRequest
import TechnicianResponse
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cr.ac.utn.appmovil.API.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUserId = findViewById<EditText>(R.id.etUserId)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val userId = etUserId.text.toString()
            val password = etPassword.text.toString()

            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val apiService = ApiClient.instance.create(ApiService::class.java)
                val request = TechnicianRequest(userId, password)

                apiService.validateAuth(request).enqueue(object : Callback<TechnicianResponse> {
                    override fun onResponse(
                        call: Call<TechnicianResponse>,
                        response: Response<TechnicianResponse>
                    ) {
                        if (response.isSuccessful) {
                            val body = response.body()
                            if (body?.data != null && body.data.isActive) {
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this@LoginActivity, body?.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<TechnicianResponse>, t: Throwable) {
                        Toast.makeText(this@LoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
