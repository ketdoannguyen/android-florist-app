package com.ntmk.myapp.view.home.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.OrderAdapter
import com.ntmk.myapp.databinding.ActivityOrderBinding
import com.ntmk.myapp.model.Order

class OrderActivity : AppCompatActivity() {
    private lateinit var binding : ActivityOrderBinding
    private lateinit var listOrder: ArrayList<Order>
    private lateinit var mAdapter: OrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        onClickStatusOrder()
        binding.linkBack.setOnClickListener {
            val i = Intent(this, HomeActivity().javaClass)
            i.putExtra("OrderToProfile",true)
            startActivity(i)
        }

        listOrder = ArrayList()
        mAdapter = OrderAdapter(this,listOrder)
        binding.recyclerOrder.layoutManager = LinearLayoutManager(this)
        binding.recyclerOrder.setHasFixedSize(true)
        binding.recyclerOrder.adapter = mAdapter
        recevieDataOtherActivity()

    }

    private fun getOrderData(paraStatus : String) {
        listOrder.removeAll(listOrder)
        mAdapter.notifyDataSetChanged()
        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
        var mDatabase = FirebaseDatabase.getInstance().getReference("FlowerOrder").child(userId)
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                    var order : Order? = dataSnapshot.getValue(Order::class.java)
                    if(order != null && order.status.equals(paraStatus)){
                        listOrder.add(order)
                        mAdapter.notifyDataSetChanged()
                    }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                mAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val order : Order = dataSnapshot.getValue(Order::class.java)!!
                for (i in 0..listOrder.size-1){
                    if(order.id == listOrder[i].id){
                        listOrder.remove(listOrder[i])
                        break
                    }
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}

        }
        mDatabase.addChildEventListener(childEventListener)
    }

//    private fun getOrderData(paraStatus : String) {
//        listOrder.removeAll(listOrder)
//        mAdapter.notifyDataSetChanged()
//        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
//        var mDatabase = FirebaseDatabase.getInstance().getReference("FlowerOrder").child(userId)
//        mDatabase.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                if (p0.exists()) {
//                    mListOrder.removeAll(mListOrder)
//                    for (data in p0.children) {
//                        val order = data.getValue(Order::class.java)
//                        mListOrder.add(order!!)
//                    }
//
//                    binding.recyclerOrder.adapter = mAdapter
//                    binding.recyclerOrder.adapter?.notifyDataSetChanged()
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.e("Cancel", error.toString())
//            }
//        })
//
//    }

    private fun onClickStatusOrder(){
        binding.orderToPay.setOnClickListener {
            StatusOrderFirst()
            binding.imgOrderToPay.setImageResource(R.drawable.wait_confirm)

            binding.tvOrderToPay.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderToPay.setTypeface(binding.tvOrderToPay.getTypeface(), Typeface.BOLD);
            getOrderData("Confirm")

        }
        binding.orderToShip.setOnClickListener {
            StatusOrderFirst()
            binding.imgOrderToShip.setImageResource(R.drawable.order_to_ship)

            binding.tvOrderToShip.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderToShip.setTypeface(binding.tvOrderToShip.getTypeface(), Typeface.BOLD);
            getOrderData("Packing")
        }
        binding.orderReceive.setOnClickListener {
            StatusOrderFirst()
            binding.imgOrderRecevie.setImageResource(R.drawable.order_recevie)

            binding.tvOrderReceive.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderReceive.setTypeface(binding.tvOrderReceive.getTypeface(), Typeface.BOLD);
            getOrderData("Delivery")
        }
        binding.orderComplete.setOnClickListener {
            StatusOrderFirst()
            binding.imgOrderComplete.setImageResource(R.drawable.order_complete)

            binding.tvOrderComplete.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderComplete.setTypeface(binding.tvOrderComplete.getTypeface(), Typeface.BOLD);
            getOrderData("Complete")
        }
        binding.orderCancel.setOnClickListener {
            StatusOrderFirst()
            binding.imgOrderCancel.setImageResource(R.drawable.order_cancel)

            binding.tvOrderCancel.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderCancel.setTypeface(binding.tvOrderCancel.getTypeface(), Typeface.BOLD);
            getOrderData("Cancel")
        }

    }
    private fun recevieDataOtherActivity(){
        var intent = intent
        var check : String? = intent.getStringExtra("StatusOrder")
        StatusOrderFirst()
        if(check.equals("Confirm")){
            binding.imgOrderToPay.setImageResource(R.drawable.wait_confirm)

            binding.tvOrderToPay.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderToPay.setTypeface(binding.tvOrderToPay.getTypeface(), Typeface.BOLD);
        }else if(check.equals("Packing")){
            binding.imgOrderToShip.setImageResource(R.drawable.order_to_ship)

            binding.tvOrderToShip.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderToShip.setTypeface(binding.tvOrderToShip.getTypeface(), Typeface.BOLD);
        }
        else if(check.equals("Delivery")){
            binding.imgOrderRecevie.setImageResource(R.drawable.order_recevie)

            binding.tvOrderReceive.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderReceive.setTypeface(binding.tvOrderReceive.getTypeface(), Typeface.BOLD);
        }else if(check.equals("Complete")){
            binding.imgOrderComplete.setImageResource(R.drawable.order_complete)

            binding.tvOrderComplete.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderComplete.setTypeface(binding.tvOrderComplete.getTypeface(), Typeface.BOLD);
        }else{
            binding.imgOrderCancel.setImageResource(R.drawable.order_cancel)

            binding.tvOrderCancel.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderCancel.setTypeface(binding.tvOrderCancel.getTypeface(), Typeface.BOLD);
        }
    }

    private fun StatusOrderFirst(){
        binding.imgOrderToPay.setImageResource(R.drawable.wait_confirm_unchecked)
        binding.imgOrderToShip.setImageResource(R.drawable.order_to_ship_unchecked)
        binding.imgOrderRecevie.setImageResource(R.drawable.order_receive_unchecked)
        binding.imgOrderComplete.setImageResource(R.drawable.order_complete_unchecked)
        binding.imgOrderCancel.setImageResource(R.drawable.order_cancel_unchecked)

        binding.tvOrderToPay.setTextColor(Color.parseColor("#888888"))
        binding.tvOrderToPay.setTypeface(null, Typeface.NORMAL);
        binding.tvOrderToShip.setTextColor(Color.parseColor("#888888"))
        binding.tvOrderToShip.setTypeface(null, Typeface.NORMAL);
        binding.tvOrderReceive.setTextColor(Color.parseColor("#888888"))
        binding.tvOrderReceive.setTypeface(null, Typeface.NORMAL);
        binding.tvOrderComplete.setTextColor(Color.parseColor("#888888"))
        binding.tvOrderComplete.setTypeface(null, Typeface.NORMAL);
        binding.tvOrderCancel.setTextColor(Color.parseColor("#888888"))
        binding.tvOrderCancel.setTypeface(null, Typeface.NORMAL);
    }
}