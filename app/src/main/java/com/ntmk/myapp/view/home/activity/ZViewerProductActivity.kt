package com.ntmk.myapp.view.home.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.controller.CartFirebase
import com.ntmk.myapp.databinding.ZViewerProductBinding
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FlowerCart
import com.ntmk.myapp.model.FollowFlower
import com.squareup.picasso.Picasso


class ZViewerProductActivity : AppCompatActivity() {
    private lateinit var binding: ZViewerProductBinding
    private lateinit var listFollowFlower : ArrayList<FollowFlower>
    private var flower : Flower = Flower()
    private var activity : String = ""
    private var checkHeart = false
    var database : CartFirebase = CartFirebase()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

        binding.linkBack.setOnClickListener {
            if(activity.equals("HomeActivity")){
                val i = Intent(this, HomeActivity::class.java)
                startActivity(i)
            }else if(activity.equals("MyLikeActivity")){
                val i = Intent(this, MyLikeActivity::class.java)
                startActivity(i)
            }

        }
        binding.btnAddProductToCart.setOnClickListener {
            onClickAddProduct()
        }
        binding.btnDecreaseqQuantity.setOnClickListener {
            if(binding.txtQuantity.text.toString().toInt()>1) {
                binding.txtQuantity.setText((binding.txtQuantity.text.toString().toInt() - 1).toString())
            }else{
                binding.txtQuantity.setText("1")
            }
        }
        binding.btnIncreaseqQuantity.setOnClickListener {
            binding.txtQuantity.setText((binding.txtQuantity.text.toString().toInt() + 1).toString())
        }
        binding.btnHeart.setOnClickListener { view ->
            var userId = FirebaseAuth.getInstance().currentUser?.uid!!
            var mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FollowFlower")
            if (!checkHeart) {
                binding.btnHeart.setImageResource(R.drawable.heart1)
                checkHeart = true
                var followFlower = FollowFlower()

                followFlower.idFlower = flower.id
                mDatabase.child(followFlower.idFlower).setValue(followFlower)
                listFollowFlower.add(followFlower)
                Toast.makeText(this, "Added to My Likes", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            } else {
                binding.btnHeart.setImageResource(R.drawable.heart)
                checkHeart = false
                Toast.makeText(this, "Removed to My Likes", Toast.LENGTH_SHORT)
                    .show()
                for (followFlower in listFollowFlower) {
                    if (followFlower.idFlower == flower.id) {
                        mDatabase.child(followFlower.idFlower).removeValue()
                        listFollowFlower.remove(followFlower)
                        return@setOnClickListener
                    }
                }
            }
        }

    }
    private fun init(){
        binding = ZViewerProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        getDataIntent()
        showDataToTextView()
        database.getDataFlowerCart()
        listFollowFlower = ArrayList()
        getLikeFlowerData()
    }
    fun getDataIntent(){
        var mBundle: Bundle = intent.extras!!
        if (mBundle == null) {
            return
        }
        flower = mBundle.get("Flower") as Flower
        activity = mBundle.getString("Activity") as String
    }
    private fun showDataToTextView(){
        binding.viewHomeName.text = flower.name
        binding.viewHomePrice.text = flower.price

        Picasso.get()
            .load(flower.img)
            .into(binding.imgVProduct);

        binding.tvInterview.text = flower.info
    }
    private fun onClickAddProduct(){
        var listFlowerCart: ArrayList<FlowerCart> = ArrayList()
        listFlowerCart = database.getListFlowerCart()
        var mFlowerCart : FlowerCart = FlowerCart()

        mFlowerCart.idFlower = flower.id.toString()!!
        mFlowerCart.name = flower.name!!
        mFlowerCart.price = flower.price!!
        mFlowerCart.img = flower.img!!

        var check : Boolean = false
        for (flower in listFlowerCart) {
            if(mFlowerCart.idFlower == flower.idFlower){
                mFlowerCart.id = flower.id
                mFlowerCart.quantity = flower.quantity + binding.txtQuantity.text.toString().toInt()
                check = true
            }
        }
        if(!check){
            if (listFlowerCart.size == 0){
                mFlowerCart.id = 0
            }else{
                mFlowerCart.id = listFlowerCart.get(listFlowerCart.size - 1).id + 1
            }
            mFlowerCart.quantity = binding.txtQuantity.text.toString().toInt()
        }

        database.addFlowerCart(mFlowerCart)
        Toast.makeText(this,"Added to cart", Toast.LENGTH_LONG).show()
    }
    fun getLikeFlowerData() {
        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
        var mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FollowFlower")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    listFollowFlower.removeAll(listFollowFlower)
                    for (data in p0.children) {
                        val followFlower = data.getValue(FollowFlower::class.java)
                        if (followFlower?.idFlower.equals(flower.id)) {
                            binding.btnHeart.setImageResource(R.drawable.heart1)
                            checkHeart = true
                        }
                        listFollowFlower.add(followFlower!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel", error.toString())
            }
        })
    }
}