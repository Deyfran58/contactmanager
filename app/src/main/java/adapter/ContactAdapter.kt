package adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.Contact

class ContactAdapter (private val context: Context, private val contacts: List<Contact>) : BaseAdapter() {

    override fun getCount(): Int = contacts.size

    override fun getItem(position: Int): Contact = contacts[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false)

        // Obtén el servicio correspondiente
        val contact = getItem(position)

        // Vincula los datos al diseño
        val contactImage = view.findViewById<ImageView>(R.id.imgPhotoItem)
        val contactName = view.findViewById<TextView>(R.id.txtContactNameItem)
        val contactAddress = view.findViewById<TextView>(R.id.txtAddressItem)


        contactName.text = contact._name
        contactAddress.text = contact._address

        // Si hay una imagen, mostrarla; si no, usa un marcador de posición
        if (contact._photo != null) {
            contactImage.setImageBitmap(contact._photo)
        } else {
            contactImage.setImageResource(R.drawable.ic_launcher_foreground)
        }

        return view
    }
}