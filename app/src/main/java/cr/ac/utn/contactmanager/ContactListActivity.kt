package cr.ac.utn.contactmanager

import Entity.Contact
import Model.ContactModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import Interface.OnItemClickListener
import Utilities.EXTRA_MESSAGE_CONTACTID
import Utilities.util
import android.content.Intent

class ContactListActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var contactListAdapter: ContactListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        val contactList = findViewById<RecyclerView>(R.id.rvContactList)
        val contactModel = ContactModel(this)
        contactListAdapter = ContactListAdapter(contactModel.getContacts(), this)
        val layoutManager = LinearLayoutManager(applicationContext)
        contactList.layoutManager = layoutManager
        contactList.adapter = contactListAdapter
        //contactList.notifyDataSetChanged()
    }

    override fun onItemClicked (contact: Contact){
       util.openActivity(this, ContactActivity::class.java, EXTRA_MESSAGE_CONTACTID, contact.Id.toString())
    }
}