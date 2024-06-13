package imd.ntub.myfrags0509

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import java.io.InputStream

class SecondFragment : Fragment() {

    private lateinit var contactDatabaseManager: ContactDatabaseManager
    private lateinit var editTextName: EditText
    private lateinit var editTextTel: EditText
    private lateinit var imageViewContact: ImageView
    private var imageUri: Uri? = null
    private var contactId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactDatabaseManager = ContactDatabaseManager(requireContext())
        contactId = arguments?.getLong("CONTACT_ID")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_second, container, false)
        editTextName = view.findViewById(R.id.editTextName)
        editTextTel = view.findViewById(R.id.editTextTel)
        imageViewContact = view.findViewById(R.id.imageViewContact)
        val buttonSave: Button = view.findViewById(R.id.buttonSave)

        val contactId = arguments?.getLong("contactId") ?: 0
        val name = arguments?.getString("name") ?: ""
        val phoneNumber = arguments?.getString("phoneNumber") ?: ""
        val imageUri = arguments?.getString("imageUri") ?: ""

        editTextName.setText(name)
        editTextTel.setText(phoneNumber)
        if (imageUri.isNotEmpty()) {
            imageViewContact.setImageURI(Uri.parse(imageUri))
        }

        imageViewContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE)
        }

        buttonSave.setOnClickListener {
            val newName = editTextName.text.toString()
            val newTel = editTextTel.text.toString()

            if (newName.isNotEmpty() && newTel.isNotEmpty()) {
                val contact = Contact(contactId, newName, newTel, this.imageUri?.toString())
                if (contactId == 0L) {
                    contactDatabaseManager.addContact(contact)
                } else {
                    // Update existing contact
                    contactDatabaseManager.updateContact(contact)
                }
                Toast.makeText(requireContext(), "你有朋友了!!", Toast.LENGTH_SHORT).show()
                editTextName.text.clear()
                editTextTel.text.clear()
                imageViewContact.setImageResource(R.drawable.icon)
                (activity as MainActivity).firstFragment.refreshContacts()
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            val imageStream: InputStream? = imageUri?.let {
                requireContext().contentResolver.openInputStream(it)
            }
            val selectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
            imageViewContact.setImageBitmap(selectedImage)
        }
    }

    companion object {
        private const val PICK_IMAGE = 1

        @JvmStatic
        fun newInstance(contactId: Long? = null) = SecondFragment().apply {
            arguments = Bundle().apply {
                putLong("CONTACT_ID", contactId ?: 0)
            }
        }
    }
}

