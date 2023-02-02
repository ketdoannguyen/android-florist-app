package com.ntmk.myapp.view.home.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.FlowerCartAdapter
import com.ntmk.myapp.databinding.ActivityCartBinding
import com.ntmk.myapp.model.FlowerCart

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var mListFlowerCart: ArrayList<FlowerCart>
    private lateinit var mAdapter: FlowerCartAdapter
    lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mListFlowerCart = ArrayList()
        mAdapter = FlowerCartAdapter(this, mListFlowerCart)
        binding.flowerCart.layoutManager = LinearLayoutManager(this)
        binding.flowerCart.setHasFixedSize(true)
        binding.flowerCart.adapter = mAdapter
        getFlowerData()

        binding.linkBack.setOnClickListener {
            val i = Intent(this, HomeActivity::class.java)
            startActivity(i)
        }

        binding.btnBuy.setOnClickListener{
            if(mListFlowerCart.size == 0){
                Toast.makeText(this, "My Cart is empty", Toast.LENGTH_SHORT).show()
            }else{
                val v = View.inflate(this, R.layout.z_viewer_payment, null)
                val builder = AlertDialog.Builder(this)
                builder.setView(v)
                val dialog = builder.create()

                v.findViewById<View>(R.id.pay_credit_card).setOnClickListener{
                    var receiptActivity  = ReceiptActivity()
                    val i = Intent(this, receiptActivity.javaClass)
                    i.putExtra("Payment","Pay Credit Card")
                    startActivity(i)
                }
                v.findViewById<View>(R.id.pay_in_cash).setOnClickListener{
                    var receiptActivity  = ReceiptActivity()
                    val i = Intent(this, receiptActivity.javaClass)
                    i.putExtra("Payment","Pay In Cash")
                    startActivity(i)
                }

                dialog.show()
                dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                dialog.window?.setGravity(Gravity.BOTTOM)
            }

        }

    }

    private fun getFlowerData() {
        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
        mDatabase = FirebaseDatabase.getInstance().getReference("FlowerCart").child(userId)
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                mListFlowerCart.removeAll(mListFlowerCart)
                if(p0.children.count() == 0){
                    binding.flowerCart.adapter = mAdapter
                    binding.flowerCart.adapter?.notifyDataSetChanged()
                    binding.txtTotalPrice.text = "$0"
                }
                if (p0.exists()) {
                    for (data in p0.children) {
                        val flower = data.getValue(FlowerCart::class.java)
                        mListFlowerCart.add(flower!!)
                    }
                    var total_Price: Int = 0
                    for (flower in mListFlowerCart) {
                        total_Price += flower.quantity * getNumberPrice(flower.price)
                    }
                    binding.txtTotalPrice.text = "$$total_Price"
                    binding.flowerCart.adapter = mAdapter
                    binding.flowerCart.adapter?.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel", error.toString())
            }
        })

    }

    fun getNumberPrice(string: String): Int {
        var number: String = ""
        number = string.replace("\$", "")
        println(number)
        return number.toInt()
    }


}