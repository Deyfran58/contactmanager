package cr.ac.utn.appmovil.interfaces

import cr.ac.utn.appmovil.identities.Contact

interface IDBManager {

    fun addContact (contact: Contact) //insert
    fun updateContact (contact: Contact) //update
    fun removeContact (id: String) //delete
    fun getAllContact(): List<Contact> //Return all contacts
    fun getContactById(id: String): Contact? //Search a specific contact by id, if id does not exist it will return null value
    fun getContactByName(name: String): Contact?

}