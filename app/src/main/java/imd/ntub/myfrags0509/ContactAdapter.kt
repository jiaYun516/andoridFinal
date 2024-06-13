package imd.ntub.myfrags0509

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle

class ContactAdapter(
    private val contactList: MutableList<Contact>,
    private val context: Context,
    private val contactDatabaseManager: ContactDatabaseManager
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIc: ImageView = itemView.findViewById(R.id.imgIc)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtTel: TextView = itemView.findViewById(R.id.txtTel)
        val imgCall: ImageView = itemView.findViewById(R.id.imgCall)
        val imgTrash: ImageView = itemView.findViewById(R.id.imgTrash)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contactList[position]
        holder.txtName.text = contact.name
        holder.txtTel.text = contact.phoneNumber
        if (contact.imageUri != null) {
            holder.imgIc.setImageURI(Uri.parse(contact.imageUri))
        } else {
            holder.imgIc.setImageResource(R.drawable.icon)
        }

        holder.imgCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${contact.phoneNumber}")
            context.startActivity(intent)
        }

        holder.imgTrash.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("我們還是朋友 對吧?")
                setMessage("確定要刪除嗎")
                setPositiveButton("刪除") { _, _ ->
                    contactDatabaseManager.deleteContact(contact.id)
                    contactList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, contactList.size)
                }
                setNegativeButton("取消", null)
                show()
            }
        }

        holder.imgIc.setOnClickListener {
            val bundle = Bundle().apply {
                putLong("contactId", contact.id)
                putString("name", contact.name)
                putString("phoneNumber", contact.phoneNumber)
                putString("imageUri", contact.imageUri)
            }
            (context as MainActivity).navigateToSecondFragment(bundle)
        }
    }
    override fun getItemCount() = contactList.size
}

