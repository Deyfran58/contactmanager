package cr.ac.utn.appmovil.contactmanager

import ContactApiService
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cr.ac.utn.appmovil.API.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class APIContactList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apicontact_list)

        recyclerView = findViewById(R.id.rvContacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchContacts()

        val fabAddContact =
            findViewById<com.google.android.material.floatingactionbutton.FloatingActionButton>(R.id.fabAddContact)
        fabAddContact.setOnClickListener {
            val intent = Intent(this, APIAddContact::class.java)
            startActivity(intent)
        }
    }

    private fun fetchContacts() {
        val api = RetrofitClient.instance.create(ContactApiService::class.java)
        api.getAllContacts().enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        contactAdapter = ContactAdapter(it)
                        recyclerView.adapter = contactAdapter
                    }
                } else {
                    Toast.makeText(
                        this@APIContactList,
                        "Error al obtener contactos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                Toast.makeText(this@APIContactList, "Error: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
