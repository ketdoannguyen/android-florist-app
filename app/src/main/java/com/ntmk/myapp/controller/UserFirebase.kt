package com.ntmk.myapp.controller

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.model.User

class UserFirebase {

    private var auth = FirebaseAuth.getInstance()
    private var list_user: ArrayList<User> = ArrayList()
    private var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    fun getListUser(): ArrayList<User>{
        return list_user
    }

    fun getData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    val user = data.getValue(User::class.java)
                    list_user.add(user as User)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel",error.toString())
            }
        })

    }
    fun addUser(user : User){
//        var userId =
        database.child((auth.currentUser?.uid!!)).setValue(user)
//        var userId = database?.child((currentUserDB?.uid!!))
//        database.child(currentUserDB?.uid.toString()).setValue(user)

//        database.child(user.id.toString()).setValue(user)
    }

    }