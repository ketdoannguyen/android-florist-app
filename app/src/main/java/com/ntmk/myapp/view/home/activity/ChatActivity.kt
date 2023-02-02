package com.ntmk.myapp.view.home.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.ChatAdapter
import com.ntmk.myapp.databinding.ActivityChatBinding
import com.ntmk.myapp.model.Chat
import com.squareup.picasso.Picasso

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    var firebaseUser: FirebaseUser? = null
    var reference: DatabaseReference? = null
    var listChat = ArrayList<Chat>()
    var checkCommunity : Boolean = true
    private lateinit var mAdapter: ChatAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        var linearLayoutManager = LinearLayoutManager(this,
            RecyclerView.VERTICAL, false)
        binding.chatRecyclerView.layoutManager = linearLayoutManager

        firebaseUser = FirebaseAuth.getInstance().currentUser
        Picasso.get()
            .load(firebaseUser!!.photoUrl)
            .into(binding.userImage)
        getChatCommunityData()
        onClickChat()

        binding.linkBack.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }
        binding.btnSendMessage.setOnClickListener {
            if(checkCommunity){
                var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Community")
                sendMessage(mDatabase)
            }else{
                var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Admin").child(firebaseUser!!.uid)
                sendMessage(mDatabase)
            }
        }
    }

    private fun sendMessage(mDatabase : DatabaseReference) {
        var message: String = binding.txtMessage.text.toString()

        if (message.isEmpty()) {
            Toast.makeText(this, "Message is empty", Toast.LENGTH_SHORT).show()
            binding.txtMessage.setText("")
        } else {
            var id = 0
            if(listChat.size == 0){
                id = 0
            }else{
                id = listChat.get(listChat.size - 1).id + 1
            }

            var chat = Chat(id,firebaseUser!!.uid,firebaseUser!!.displayName.toString(),firebaseUser!!.photoUrl.toString(), message)
            mDatabase.child(id.toString()).setValue(chat)
            binding.txtMessage.setText("")
        }
    }
    private fun onClickChat(){
        binding.txt1.setOnClickListener {
            binding.txt1.setTextColor(Color.parseColor("#FF6600"))
            binding.txt1.setTypeface(binding.txt1.getTypeface(), Typeface.BOLD);

            binding.txt2.setTextColor(Color.parseColor("#888888"))
            binding.txt2.setTypeface(null, Typeface.NORMAL);

            binding.view1.visibility = View.VISIBLE
            binding.view2.visibility = View.GONE
            getChatCommunityData()
            checkCommunity = true
        }
        binding.txt2.setOnClickListener {
            binding.txt2.setTextColor(Color.parseColor("#FF6600"))
            binding.txt2.setTypeface(binding.txt1.getTypeface(), Typeface.BOLD);

            binding.txt1.setTextColor(Color.parseColor("#888888"))
            binding.txt1.setTypeface(null, Typeface.NORMAL);

            binding.view2.visibility = View.VISIBLE
            binding.view1.visibility = View.GONE
            getChatAdminData()
            checkCommunity = false
        }
    }
    fun getChatCommunityData() {
        listChat.clear()
        var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Community")
        mAdapter = ChatAdapter(applicationContext, listChat,true)
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

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

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

    }
    fun getChatAdminData() {
        listChat.clear()
        var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Admin").child(firebaseUser!!.uid)
        mAdapter = ChatAdapter(applicationContext, listChat,false)
        binding.chatRecyclerView.adapter = mAdapter
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                var chat : Chat? = dataSnapshot.getValue(Chat::class.java)
                if(chat != null){
                    listChat.add(chat)
                    binding.chatRecyclerView.scrollToPosition(listChat.size - 1)
                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

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

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mDatabase.addChildEventListener(childEventListener)

    }
}