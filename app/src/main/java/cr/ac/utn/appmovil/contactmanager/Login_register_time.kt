package cr.ac.utn.appmovil.contactmanager

import adapter.loginAdapter
import android.os.Bundle
import android.widget.ListView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import cr.ac.utn.appmovil.contactmanager.databinding.ActivityLoginRegisterTimeBinding
import cr.ac.utn.appmovil.identities.LoginEvent
import data.SQlManager

class Login_register_time : AppCompatActivity() {


    private lateinit var dbManager: SQlManager
    private lateinit var lstLoginEvents: ListView
    private lateinit var loginEvents: List<LoginEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_register_time)

        lstLoginEvents = findViewById(R.id.list_event)
        dbManager = SQlManager(this)

        // Obtener los registros de inicio de sesiónn
        loginEvents = dbManager.getAllLoginEvents()

        // Configurar el adaptador para mostrar los registros
        val adapter = loginAdapter(this, R.layout.activity_login_register_time, loginEvents)
        lstLoginEvents.adapter = adapter
    }
}