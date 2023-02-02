package com.ntmk.myapp.view.admin.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.ChatAdminAdapter
import com.ntmk.myapp.adapters.OrderAdapter
import com.ntmk.myapp.adapters.OrderAdminAdapter
import com.ntmk.myapp.databinding.ActivityOrderBinding
import com.ntmk.myapp.databinding.FragmentManageOrderAdminBinding
import com.ntmk.myapp.model.Chat
import com.ntmk.myapp.model.Order

class ManageOrderAdminFragment : Fragment() {

    private lateinit var binding : FragmentManageOrderAdminBinding
    private lateinit var listOrder: ArrayList<Order>
    private lateinit var mAdapter: OrderAdminAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_manage_order_admin, container, false)
        listOrder = ArrayList()
        mAdapter = OrderAdminAdapter(this,requireContext(),listOrder)
        binding.recyclerOrder.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerOrder.setHasFixedSize(true)
        binding.recyclerOrder.adapter = mAdapter
        onClickStatusOrder()
        return binding.root
    }

    private fun getOrderData(paraStatus : String) {
        listOrder.removeAll(listOrder)
        mAdapter.notifyDataSetChanged()
        var mDatabase = FirebaseDatabase.getInstance().getReference("FlowerOrder")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                for(dataChildren in dataSnapshot.children){
                    var order : Order? = dataChildren.getValue(Order::class.java)
                    if(order != null && order.status.equals(paraStatus)){
                        listOrder.add(order)
                        mAdapter.notifyDataSetChanged()
                    }

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

    private fun onClickStatusOrder(){
//        Start
        binding.imgOrderToPay.setImageResource(R.drawable.wait_confirm)

        binding.tvOrderToPay.setTextColor(Color.parseColor("#FF6600"))
        binding.tvOrderToPay.setTypeface(binding.tvOrderToPay.getTypeface(), Typeface.BOLD);
        getOrderData("Confirm")
        binding.orderToPay.setOnClickListener {
            statusOrderFirst()
            binding.imgOrderToPay.setImageResource(R.drawable.wait_confirm)

            binding.tvOrderToPay.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderToPay.setTypeface(binding.tvOrderToPay.getTypeface(), Typeface.BOLD);
            getOrderData("Confirm")
        }
        binding.orderToShip.setOnClickListener {
            statusOrderFirst()
            binding.imgOrderToShip.setImageResource(R.drawable.order_to_ship)

            binding.tvOrderToShip.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderToShip.setTypeface(binding.tvOrderToShip.getTypeface(), Typeface.BOLD);
            getOrderData("Packing")
        }
        binding.orderReceive.setOnClickListener {
            statusOrderFirst()
            binding.imgOrderRecevie.setImageResource(R.drawable.order_recevie)

            binding.tvOrderReceive.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderReceive.setTypeface(binding.tvOrderReceive.getTypeface(), Typeface.BOLD);
            getOrderData("Delivery")
        }
        binding.orderComplete.setOnClickListener {
            statusOrderFirst()
            binding.imgOrderComplete.setImageResource(R.drawable.order_complete)

            binding.tvOrderComplete.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderComplete.setTypeface(binding.tvOrderComplete.getTypeface(), Typeface.BOLD);
            getOrderData("Complete")
        }
        binding.orderCancel.setOnClickListener {
            statusOrderFirst()
            binding.imgOrderCancel.setImageResource(R.drawable.order_cancel)

            binding.tvOrderCancel.setTextColor(Color.parseColor("#FF6600"))
            binding.tvOrderCancel.setTypeface(binding.tvOrderCancel.getTypeface(), Typeface.BOLD);
            getOrderData("Cancel")
        }

    }

    private fun statusOrderFirst(){
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

    public fun onClickLayoutChat(name:String,imgSender:String,idSender:String){
        val chatFragment = ChatFragment(name,imgSender,idSender,false)
        fragmentManager?.beginTransaction()?.apply {
            replace(
                com.ntmk.myapp.R.id.context_fragment,
                chatFragment,
                ChatFragment::class.java.simpleName
            )
                .addToBackStack(null).commit()
        }
    }
}