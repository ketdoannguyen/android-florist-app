package com.ntmk.myapp.controller

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.model.FlowerCart
import com.ntmk.myapp.model.User

class CartFirebase {
    private var listFlowerCart: ArrayList<FlowerCart> = ArrayList()
    private var database: DatabaseReference
    var userId = FirebaseAuth.getInstance().currentUser?.uid!!

    init {
        database = FirebaseDatabase.getInstance().getReference("FlowerCart").child(userId)
    }

    fun getListFlowerCart(): ArrayList<FlowerCart>{
        return listFlowerCart
    }

    fun getDataFlowerCart() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    var flower = data.getValue(FlowerCart::class.java)
                    listFlowerCart.add(flower as FlowerCart)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel",error.toString())
            }
        })

    }
    fun addFlowerCart(mFlowerCart : FlowerCart){
        database.child(mFlowerCart.id.toString()).setValue(mFlowerCart).addOnSuccessListener {
            //Toast.makeText(this, "Registration success", Toast.LENGTH_SHORT).show()
        }.addOnCanceledListener {
            //Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
        }

    }
    }