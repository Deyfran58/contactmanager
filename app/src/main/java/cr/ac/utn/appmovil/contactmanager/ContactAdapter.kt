package cr.ac.utn.appmovil.contactmanager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import cr.ac.utn.appmovil.identities.Contact
import cr.ac.utn.appmovil.contactmanager.R

class ContactAdapter(
    private val context: Context,
    private val dataSource: List<Contact>
) : BaseAdapter() {

    override fun getCount(): Int = dataSource.size

    override fun getItem(position: Int): Any = dataSource[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_contact, parent, false)

        val txtContactNameItem = rowView.findViewById<TextView>(R.id.txtContactNameItem)
        val txtAddressItem = rowView.findViewById<TextView>(R.id.txtAddressItem)
        val txtPhoneItem = rowView.findViewById<TextView>(R.id.txtPhoneItem)
        val imgPhotoItem = rowView.findViewById<ImageView>(R.id.imgPhotoItem)

        // Obtener el objeto Contact actual
        val contact = dataSource[position]

        // Asignar datos a las vistas
        txtContactNameItem.text = contact.FullName
        txtAddressItem.text = contact.Address
        txtPhoneItem.text = contact.Phone

        // Manejar si no hay una foto asignada
        if (!contact.Photo.isNullOrEmpty()) {
            imgPhotoItem.setImageBitmap(decodeBase64ToBitmap(contact.Photo))
        } else {
            imgPhotoItem.setImageResource(R.drawable.ic_launcher_foreground) // Imagen predeterminada
        }

        return rowView
    }

    // Método para decodificar una cadena Base64 en un Bitmap
    private fun decodeBase64ToBitmap(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }
}
