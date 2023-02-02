package com.ntmk.myapp.view.home.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.FlowerCartAdapter
import com.ntmk.myapp.adapters.FollowFlowerAdapter
import com.ntmk.myapp.databinding.ActivityHomeBinding
import com.ntmk.myapp.databinding.ActivityMyLikeBinding
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FlowerCart
import com.ntmk.myapp.model.FollowFlower

class MyLikeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyLikeBinding
    private lateinit var mListFlower: ArrayList<Flower>
    private lateinit var mAdapter: FollowFlowerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        binding.linkBack.setOnClickListener {
            val i = Intent(this, HomeActivity().javaClass)
            i.putExtra("OrderToProfile",true)
            startActivity(i)
        }
    }

    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_like)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        mListFlower = ArrayList()
        mAdapter = FollowFlowerAdapter(this, mListFlower)
        binding.recyclerFollowFlower.layoutManager = LinearLayoutManager(this)
        binding.recyclerFollowFlower.setHasFixedSize(true)
        binding.recyclerFollowFlower.adapter = mAdapter
        getFollowFlowerData()
    }

    private fun getFollowFlowerData() {
        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
        var mDatabase =
            FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FollowFlower")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                mListFlower.removeAll(mListFlower)
                if(p0.children.count() == 0){
                    binding.recyclerFollowFlower.adapter = mAdapter
                    binding.recyclerFollowFlower.adapter?.notifyDataSetChanged()
                }
                if (p0.exists()) {
                    var count = 0
                    for (data in p0.children) {
                        count += 1
                        val followFlower = data.getValue(FollowFlower::class.java)
                        var mDatabase = FirebaseDatabase.getInstance().getReference("Flowers").child(followFlower!!.idFlower)
                        mDatabase.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p1: DataSnapshot) {
                                if (p1.exists()) {
                                    val flower = p1.getValue(Flower::class.java)
                                    mListFlower.add(flower!!)
                                    if(count == p0.children.count()){
                                        binding.recyclerFollowFlower.adapter = mAdapter
                                        binding.recyclerFollowFlower.adapter?.notifyDataSetChanged()
                                    }
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }
}