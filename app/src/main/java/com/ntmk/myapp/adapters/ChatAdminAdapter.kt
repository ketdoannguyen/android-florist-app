package com.ntmk.myapp.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.ntmk.myapp.R
import com.ntmk.myapp.model.Chat
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ChatAdminAdapter(var context: Context, var listChat: ArrayList<Chat>,var idSender:String,var isCommunity : Boolean) :
    RecyclerView.Adapter<ChatAdminAdapter.ViewHolder>() {
    var mContext: Context? = context
    private val MESSAGE_TYPE_LEFT = 0
    private val MESSAGE_TYPE_RIGHT = 1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNameSender: TextView = view.findViewById(R.id.tvNameSender)
        val txtMessage: TextView = view.findViewById(R.id.tvMessage)
        val imgUser: CircleImageView = view.findViewById(R.id.userImageMessage)
        val layout: LinearLayout = view.findViewById(R.id.layoutChat)

        public fun popupMenus(v: View, chat: Chat) {
            var popupMenu = PopupMenu(context, v, Gravity.RIGHT)
            popupMenu.inflate(R.menu.show_menu_chat1)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menuDelete -> {
                        if(isCommunity){
                            var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage")
                                .child("Community")
                            mDatabase.child(chat.id.toString()).removeValue()
                        }else{
                            var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage")
                                .child("Admin").child(idSender)
                            mDatabase.child(chat.id.toString()).removeValue()
                        }

                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menuReport -> {
                        Toast.makeText(mContext, "Reported", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }

            }
            popupMenu.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == MESSAGE_TYPE_RIGHT) {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_right_chat, parent, false)
            return ViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_left_chat, parent, false)
            return ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat: Chat = listChat[position]
        holder.txtMessage.setText(chat.message)
        holder.txtNameSender.text = chat.nameSender
        Picasso.get()
            .load(chat.imgSender)
            .into(holder.imgUser)

        holder.txtMessage.setOnClickListener {
            holder.popupMenus(it, chat)
        }
    }

    override fun getItemCount(): Int {
        return listChat.size
    }

    override fun getItemViewType(position: Int): Int {
        if (listChat[position].idSender == "Admin") {
            return MESSAGE_TYPE_RIGHT
        } else {
            return MESSAGE_TYPE_LEFT
        }
    }
}