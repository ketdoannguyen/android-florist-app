package com.ntmk.myapp.view.admin.fragment

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.ntmk.myapp.adapters.ListAdminUserAdapter
import com.ntmk.myapp.adapters.ListChatAdminAdapter
import com.ntmk.myapp.databinding.FragmentManageChatAdminBinding
import com.ntmk.myapp.databinding.ZListChatAdminBinding
import com.ntmk.myapp.model.Chat
import com.ntmk.myapp.model.User
import com.ntmk.myapp.view.home.fragment.SettingFragment


class ManageChatAdminFragment : Fragment() {

    private lateinit var rView: RecyclerView
    private lateinit var listChat: ArrayList<Chat>
    private lateinit var ChatAdapter: ListChatAdminAdapter
    private lateinit var binding: FragmentManageChatAdminBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageChatAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        binding.btnAdd.setOnClickListener { view -> showDialogUpdateProfile() }
        showChatAdmin()
        return root
    }

    public fun onClickLayoutChat(name:String,imgSender:String,idSender:String){
        val chatFragment = ChatFragment(name,imgSender,idSender,true)
        fragmentManager?.beginTransaction()?.apply {
            replace(
                com.ntmk.myapp.R.id.context_fragment,
                chatFragment,
                ChatFragment::class.java.simpleName
            )
                .addToBackStack(null).commit()
        }
    }

    private fun showChatAdmin() {
        listChat = ArrayList()
        rView = binding.listChat
        ChatAdapter = ListChatAdminAdapter(this,requireContext(), listChat)
        val layout = LinearLayoutManager(context)
        rView!!.setLayoutManager(layout)
        rView!!.setAdapter(ChatAdapter)
        var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if ("Community".compareTo(dataSnapshot.key.toString()) == 0){
                    var max = 0
                    for (child in dataSnapshot.children){
                        var i = child.key?.toInt()
                        if (i?:0 > max){
                            max = i?:0
                        }
                    }
                    var imgSender =
                        "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/community.png?alt=media&token=1854368e-1732-440f-b283-29382e63d64c"
                    var nameSender = "Community"
                    var message = dataSnapshot.child((max.toString()).toString()).child("message").getValue(String::class.java)!!
                    if (message.length > 18){
                        message = message.subSequence(0,18) as String + "..."
                    }
                    listChat.add(Chat("Admin",nameSender, imgSender, message))
                    ChatAdapter.notifyDataSetChanged()
                }
                if ("Admin".compareTo(dataSnapshot.key.toString()) == 0){
                    for(children in dataSnapshot.children){
                        var max = 0
                        for (child in children.children){
                            var i = child.key?.toInt()
                            if (i?:0 > max){
                                max = i?:0
                            }
                        }
                        val mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users").child(children.key.toString())
                        mDatabaseUser.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshotUser: DataSnapshot) {
                                var img =""
                                var name = ""
                                var id = ""
                                for (postSnapshot in dataSnapshotUser.children) {
                                    if(postSnapshot.key.toString() == "img"){
                                        img = postSnapshot.getValue(String::class.java)!!
                                    }
                                    if(postSnapshot.key.toString() == "userName"){
                                        name = postSnapshot.getValue(String::class.java)!!
                                    }
                                    if(postSnapshot.key.toString() == "userId"){
                                        id = postSnapshot.getValue(String::class.java)!!
                                    }
                                }
//                                children.childrenCount-1
                                var message : String = children.child((max.toString()).toString()).child("message").getValue(String::class.java)!!
                                if (message.length > 18){
                                    message = message.subSequence(0,18) as String + "..."
                                }
                                listChat.add(Chat(id,name,img, message))
                                ChatAdapter.notifyDataSetChanged()
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
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
                ChatAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        mDatabase.addChildEventListener(childEventListener)

    }
}