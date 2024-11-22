package cr.ac.utn.appmovil.interfaces

import cr.ac.utn.appmovil.identities.Contact

interface IDBManager {
    fun update (contact: Contact) //update
    fun add (contact: Contact) //insert
    fun remove (id: String) //delete
    fun getAll(): List<Contact> //Return all contacts
    fun getById(id: String): Contact? //Search a specific contact by id, if id does not exist it will return null value
}