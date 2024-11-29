package cr.ac.utn.appmovil.model

import android.content.Context
import cr.ac.utn.appmovil.api.ContactAPI
import cr.ac.utn.appmovil.identities.Contact
import org.json.JSONArray
import org.json.JSONObject

class ContactModel(context: Context) {
    private val contactAPI = ContactAPI()

    fun addContact(contact: Contact): String {
        val payload = JSONObject()
        payload.put("personId", contact.Id.toInt())
        payload.put("name", contact.Name)
        payload.put("lastName", contact.LastName)
        payload.put("provinceCode", contact.Country.toInt()) // Assuming Country stores provinceCode
        payload.put("birthdate", "2000-01-01") // Replace with actual data
        payload.put("gender", "M") // Replace with actual data

        return contactAPI.addContact(payload)
    }

    fun updateContact(contact: Contact): String {
        val payload = JSONObject()
        payload.put("personId", contact.Id.toInt())
        payload.put("name", contact.Name)
        payload.put("lastName", contact.LastName)
        payload.put("provinceCode", contact.Country.toInt())
        payload.put("birthdate", "2000-01-01")
        payload.put("gender", "M")

        return contactAPI.updateContact(payload)
    }

    fun removeContact(id: String): String {
        val payload = JSONObject()
        payload.put("personId", id.toInt())

        return contactAPI.deleteContact(payload)
    }

    fun getContacts(): List<Contact> {
        val response = contactAPI.getAllContacts()
        val contacts = JSONArray(response)
        val contactList = mutableListOf<Contact>()

        for (i in 0 until contacts.length()) {
            val jsonContact = contacts.getJSONObject(i)
            val contact = Contact.fromJson(jsonContact)
            contactList.add(contact)
        }

        return contactList
    }

    fun getContact(id: String): Contact {
        val response = contactAPI.getContactById(id)
        val jsonContact = JSONObject(response)

        return Contact.fromJson(jsonContact)
    }

    fun getContactNames(): List<String> {
        val contacts = getContacts()
        return contacts.map { it.FullName }
    }
}
