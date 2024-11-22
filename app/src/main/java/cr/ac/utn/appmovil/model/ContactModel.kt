package cr.ac.utn.appmovil.model

import android.content.Context
import cr.ac.utn.appmovil.contactmanager.R
import cr.ac.utn.appmovil.identities.Contact
import data.ContactDbHelper

class ContactModel(private val context: Context) {

    private val dbHelper = ContactDbHelper(context)

    fun addContact(contact: Contact) {
        try {
            dbHelper.add(contact)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(context.getString(R.string.msgErrorAddingContact))
        }
    }

    fun updateContact(contact: Contact) {
        try {
            dbHelper.update(contact)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(context.getString(R.string.msgErrorUpdatingContact))
        }
    }

    fun removeContact(id: String) {
        try {
            dbHelper.remove(id)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(context.getString(R.string.msgErrorDeletingContact))
        }
    }

    fun getContacts(): List<Contact> {
        return try {
            dbHelper.getAll()
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(context.getString(R.string.msgErrorFetchingContacts))
        }
    }

    fun getContact(id: String): Contact {
        return try {
            dbHelper.getById(id)
                ?: throw Exception(context.getString(R.string.msgNotFoundContact))
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(context.getString(R.string.msgErrorFetchingContact))
        }
    }

    fun getContactNames(): List<String> {
        return try {
            dbHelper.getAll().map { "${it.Name} ${it.LastName}" }
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(context.getString(R.string.msgErrorFetchingContactNames))
        }
    }
}
