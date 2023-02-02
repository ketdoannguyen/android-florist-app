package com.ntmk.myapp.view.admin.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.ChatAdapter
import com.ntmk.myapp.adapters.ChatAdminAdapter
import com.ntmk.myapp.databinding.FragmentChatBinding
import com.ntmk.myapp.databinding.FragmentProfileBinding
import com.ntmk.myapp.model.Chat
import com.ntmk.myapp.view.home.activity.HomeActivity
import com.ntmk.myapp.view.home.fragment.SettingFragment
import com.squareup.picasso.Picasso

class ChatFragment(var name:String,var imgSender:String,var idSender : String,var isChatAdmin:Boolean) : Fragment() {
    private lateinit var binding: FragmentChatBinding
    var reference: DatabaseReference? = null
    var listChat = ArrayList<Chat>()
    private lateinit var mAdapter: ChatAdminAdapter
    var isCommunity = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)
        binding.nameSender.text = name
        Picasso.get()
            .load(imgSender)
            .into(binding.imgSender)

        binding.linkBack.setOnClickListener {
            if(isChatAdmin){
                val ManageChatAdminFragment = ManageChatAdminFragment()
                fragmentManager?.beginTransaction()?.apply {
                    replace(
                        com.ntmk.myapp.R.id.context_fragment,
                        ManageChatAdminFragment,
                        ManageChatAdminFragment::class.java.simpleName
                    )
                        .addToBackStack(null).commit()
                }
            }else{
                val ManageOrderAdminFragment = ManageOrderAdminFragment()
                fragmentManager?.beginTransaction()?.apply {
                    replace(
                        com.ntmk.myapp.R.id.context_fragment,
                        ManageOrderAdminFragment,
                        ManageOrderAdminFragment::class.java.simpleName
                    )
                        .addToBackStack(null).commit()
                }
            }
        }
        binding.btnSendMessage.setOnClickListener {
            if(name.compareTo("Community")==0){
                var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Community")
                sendMessage(mDatabase)
            }else{
                var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Admin").child(idSender)
                sendMessage(mDatabase)
            }
        }

        var linearLayoutManager = LinearLayoutManager(requireContext(),
            RecyclerView.VERTICAL, false)
        binding.chatRecyclerView.layoutManager = linearLayoutManager
        getChatData()
        return binding.root
    }
    private fun sendMessage(mDatabase : DatabaseReference) {
        var message: String = binding.txtMessage.text.toString()

        if (message.isEmpty()) {
            Toast.makeText(requireContext(), "Message is empty", Toast.LENGTH_SHORT).show()
            binding.txtMessage.setText("")
        } else {
            var id = 0
            if(listChat.size == 0){
                id = 0
            }else{
                id = listChat.get(listChat.size - 1).id + 1
            }
            val img = "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/community.png?alt=media&token=1854368e-1732-440f-b283-29382e63d64c"
            var chat = Chat(id,"Admin","Admin",img, message)
            mDatabase.child(id.toString()).setValue(chat)
            binding.txtMessage.setText("")
        }
    }
    fun getChatData() {
        listChat.clear()
        if(name.compareTo("Community")==0){
            var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Community")
            mAdapter = ChatAdminAdapter(requireContext(), listChat,idSender,true)
            binding.chatRecyclerView.adapter = mAdapter
            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    var chat : Chat? = dataSnapshot.getValue(Chat::class.java)
                    if(chat != null){
                        listChat.add(chat)
                        binding.chatRecyclerView.scrollToPosition(listChat.size - 1);
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    var chat : Chat? = dataSnapshot.getValue(Chat::class.java)
                    if(chat == null || listChat == null || listChat.isEmpty()){
                        return
                    }
                    for (i in 0..listChat.size-1){
                        if(chat.id == listChat[i].id){
                            listChat.remove(listChat[i])
                            break
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            mDatabase.addChildEventListener(childEventListener)
        }else{
            var mDatabaseAdmin = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Admin").child(idSender)
            mAdapter = ChatAdminAdapter(requireContext(), listChat,idSender,false)
            binding.chatRecyclerView.adapter = mAdapter
            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    var chat : Chat? = dataSnapshot.getValue(Chat::class.java)
                    if(chat != null){
                        listChat.add(chat)
                        binding.chatRecyclerView.scrollToPosition(listChat.size - 1);
                        mAdapter.notifyDataSetChanged()
                    }
                }

                override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {}

                override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                    var chat : Chat? = dataSnapshot.getValue(Chat::class.java)
                    if(chat == null || listChat == null || listChat.isEmpty()){
                        return
                    }
                    for (i in 0..listChat.size-1){
                        if(chat.id == listChat[i].id){
                            listChat.remove(listChat[i])
                            break
                        }
                    }
                    mAdapter.notifyDataSetChanged()
                }

                override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

                override fun onCancelled(databaseError: DatabaseError) {}
            }
            mDatabaseAdmin.addChildEventListener(childEventListener)
        }


    }

}