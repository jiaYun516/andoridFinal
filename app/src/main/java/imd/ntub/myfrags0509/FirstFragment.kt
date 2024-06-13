package imd.ntub.myfrags0509

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FirstFragment : Fragment() {
    private lateinit var contactDatabaseManager: ContactDatabaseManager
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contacts: MutableList<Contact>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactDatabaseManager = ContactDatabaseManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_first, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        contacts = contactDatabaseManager.getAllContacts().toMutableList()
        contactAdapter = ContactAdapter(contacts, requireContext(), contactDatabaseManager)
        recyclerView.adapter = contactAdapter
        return view
    }

    fun refreshContacts() {
        contacts.clear()
        contacts.addAll(contactDatabaseManager.getAllContacts())
        contactAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FirstFragment()
    }
}
