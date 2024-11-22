package cr.ac.utn.appmovil.identities

interface ContactDBManager {
    fun add (obj: Contact)
    fun update (obj: Contact)
    fun remove (id: String)
    fun getAll(): List<Contact>
    fun getByid(id: String): Contact?

}