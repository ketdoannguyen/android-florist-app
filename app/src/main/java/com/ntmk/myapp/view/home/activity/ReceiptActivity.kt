package com.ntmk.myapp.view.home.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chaos.view.PinView
import com.google.firebase.auth.*
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.ReceiptAdapter
import com.ntmk.myapp.databinding.ActivityReceiptBinding
import com.ntmk.myapp.model.FlowerCart
import com.ntmk.myapp.model.Order
import com.ntmk.myapp.model.User
import com.ntmk.myapp.view.home.fragment.ProfileFragment
import java.text.SimpleDateFormat
import java.util.*


class ReceiptActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReceiptBinding
    private lateinit var mListFlowerCart: ArrayList<FlowerCart>
    private lateinit var mAdapter: ReceiptAdapter
    private lateinit var userAuth : FirebaseUser
    private lateinit var phoneNumber: String
    private lateinit var progressDialog: ProgressDialog
    private lateinit var payment : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiptBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        mListFlowerCart = ArrayList()
        mAdapter = ReceiptAdapter(mListFlowerCart)
        binding.flowerCart.layoutManager = LinearLayoutManager(this)
        binding.flowerCart.setHasFixedSize(true)
        binding.flowerCart.adapter = mAdapter
        getFlowerData()
        init()

        // ProgressDialog
        progressDialog.setTitle("Please wail")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.linkBack.setOnClickListener {
            val i = Intent(this, CartActivity::class.java)
            startActivity(i)
        }

        binding.btnConfirm.setOnClickListener {
            phoneNumber = userAuth?.phoneNumber.toString()
            if(phoneNumber.equals("")){
                var database = FirebaseDatabase.getInstance().getReference("Users")
                var list_user = ArrayList<User>()

                database.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        for (data in p0.children) {
                            var user = data.getValue(User::class.java) as User
                            list_user.add(user as User)
                        }
                        val v =
                            View.inflate(this@ReceiptActivity, R.layout.verify_email_fragment, null)
                        val builder = AlertDialog.Builder(this@ReceiptActivity)
                        builder.setView(v)
                        val dialog = builder.create()
                        var id_otp = v.findViewById<PinView>(R.id.id_otp);
                        v.findViewById<View>(R.id.btn_submit).setOnClickListener {
                            progressDialog.setTitle("Paying...")
                            progressDialog.show()
                            if (id_otp.text.toString().equals("123456")) {
                                Toast.makeText(
                                    this@ReceiptActivity,
                                    "Payment successful",
                                    Toast.LENGTH_SHORT
                                ).show()
                                intent()
                                var userId = userAuth?.uid!!
                                var mDatabaseCart = FirebaseDatabase.getInstance().getReference("FlowerCart").child(userId)
                                mDatabaseCart.removeValue()

                                var mDatabaseOrder = FirebaseDatabase.getInstance().getReference("FlowerOrder").child(userId)
                                var order  = Order();
                                order.listFlower = mListFlowerCart
                                order.status = "Confirm"
                                order.idUser = userAuth.uid
                                order.nameUser = "Name user : "+userAuth.displayName.toString()
                                order.payment = payment
                                val sdf1 = SimpleDateFormat("HH:mm:ss dd/MM/yyyy")
                                val sdf2 = SimpleDateFormat("ddMMyyyy")
                                val currentDate = sdf1.format(Date())
                                order.timeOrder = currentDate

                                order.id = sdf2.format(Date()) + randomAlphaNumeric();
                                order.orderTotal = binding.txtTotalPrice.text.toString()

                                mDatabaseOrder.child(order.id.toString()).setValue(order)

                            } else {
                                Toast.makeText(
                                    this@ReceiptActivity,
                                    "OTP is incorrect",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        dialog.show()
                        dialog.window?.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                        )

                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                        dialog.window?.setGravity(Gravity.BOTTOM)
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

            }else{
                Toast.makeText(
                    this@ReceiptActivity,
                    "Please update user information",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val alphaUpperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val digits = "0123456789" // 0-9
    private val ALPHA_NUMERIC: String = alphaUpperCase + digits
    private fun randomAlphaNumeric(): String? {
        val sb = StringBuilder()
        for (i in 0 until 8) {
            if(i < 4){
                val number: Int = Random().nextInt(alphaUpperCase.length)

                val ch: Char = alphaUpperCase.get(number)
                sb.append(ch)
            }else if(i == 4 ){
                val number: Int = Random().nextInt(digits.length)
                val ch: Char = digits.get(number)
                sb.append(ch)
            }else{
                val number: Int = Random().nextInt(ALPHA_NUMERIC.length)
                val ch: Char = ALPHA_NUMERIC.get(number)
                sb.append(ch)
            }
        }
        return sb.toString()
    }


    private fun intent() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun init() {
        progressDialog = ProgressDialog(this)
        userAuth = FirebaseAuth.getInstance().currentUser!!
        payment = recevieDataOtherActivity()
        getAddressUser()
    }

    fun getFlowerData() {
        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
        var mDatabase = FirebaseDatabase.getInstance().getReference("FlowerCart").child(userId)
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    mListFlowerCart.removeAll(mListFlowerCart)
                    for (data in p0.children) {
                        val flower = data.getValue(FlowerCart::class.java)
                        mListFlowerCart.add(flower!!)
                    }
                    var total_Price: Int = 0
                    for (flower in mListFlowerCart) {
                        total_Price = total_Price + flower.quantity * getNumberPrice(flower.price)
                    }
                    binding.txtTotalPrice.setText("$" + total_Price.toString())
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
        var number = ""
        number = string.replace("\$", "")
        println(number)
        return number.toInt()
    }

    fun getAddressUser() {
        var database = FirebaseDatabase.getInstance().getReference("Users")
        var list_user = ArrayList<User>()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    var user = data.getValue(User::class.java)
                    list_user.add(user as User)
                }
                var emailAuth = userAuth?.email
                for (user in list_user) {
                    if (user.email.equals(emailAuth)) {
                        binding.txtAddress.setText(user.address)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel", error.toString())
            }
        })
    }
    private fun recevieDataOtherActivity():String{
        var intent = intent
        var payment : String? = intent.getStringExtra("Payment")
        return payment.toString()
    }
}
