package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.LoginEvent

class loginAdapter(context: Context, resource: Int, objects: List<LoginEvent>) :
    ArrayAdapter<LoginEvent>(context, resource, objects) {

    private val inflater = LayoutInflater.from(context)
    private val resourceLayout = resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View = convertView ?: inflater.inflate(resourceLayout, parent, false)

        val loginEvent = getItem(position)

        val txtUserId = view.findViewById<TextView>(R.id.txt_id)
        val txtTime = view.findViewById<TextView>(R.id.txtTime)

        txtUserId.text = "Usuario: ${loginEvent?.userId}"
        txtTime.text = "Fecha y hora: ${loginEvent?.loginTime}"

        return view
    }
}