package com.ntmk.myapp.adapters

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.controller.CartFirebase
import com.ntmk.myapp.databinding.ZListCartTabViewBinding
import com.ntmk.myapp.databinding.ZListOrderBinding
import com.ntmk.myapp.model.Chat
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FlowerCart
import com.ntmk.myapp.model.Order
import com.ntmk.myapp.view.admin.fragment.ManageOrderAdminFragment

class OrderAdminAdapter(var fragment : ManageOrderAdminFragment, var context: Context, var listOrder: ArrayList<Order>) :
    RecyclerView.Adapter<OrderAdminAdapter.listOrderViewHolder>() {
    var mContext: Context? = context

    inner class listOrderViewHolder(var v: ZListOrderBinding) :
        RecyclerView.ViewHolder(v.root) {
        var layout : LinearLayout? = null
        var rView: RecyclerView? = null
        var txtTimeOrder: TextView? = null
        var txtOrderTotal: TextView? = null
        var index : Int = 0

        public  lateinit var mListFlowerCart: ArrayList<FlowerCart>
        public lateinit var mAdapter: ReceiptAdapter

        init {
            layout = v.layoutOrder
            txtTimeOrder = v.txtTime
            txtOrderTotal = v.txtMoney
            rView = v.flowerOrder
            mListFlowerCart = ArrayList()

        }
        fun onClick(name:String,imgSender: String,idSender: String){
            fragment.onClickLayoutChat(name,imgSender,idSender)
        }

        public fun popupMenus1(v: View, order: Order) {
            var popupMenu = PopupMenu(context, v, Gravity.RIGHT)
            popupMenu.inflate(R.menu.menu_admin_order)
            if(order.status.equals("Cancel")){
                popupMenu.menu.get(3).setVisible(false)
                popupMenu.menu.get(1).setVisible(false)
            }else if(order.status.equals("Confirm")){
                popupMenu.menu.get(1).setTitle("Confirm order")
            }else if(order.status.equals("Packing")){
                popupMenu.menu.get(1).setTitle("Confirm packed")
            }else if(order.status.equals("Delivery")){
                popupMenu.menu.get(1).setVisible(false)
            }

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.menuDetail -> {
                        Toast.makeText(mContext, "Detail", Toast.LENGTH_SHORT).show()
                        true
                    }

                    R.id.menuConfirm -> {
                        var mDatabase = FirebaseDatabase.getInstance().getReference("FlowerOrder").child(order.idUser).child(order.id)
                        if(order.status == "Confirm"){
                            order.status = "Packing"
                        }else if(order.status == "Packing"){
                            order.status = "Delivery"
                        }
                        mDatabase.setValue(order)

                        var message = "Your order " + order.id + " has been "+order.status?.lowercase()+" !!"
                        sendMessage(order.idUser,message)
                        Toast.makeText(mContext, "Succesful", Toast.LENGTH_SHORT).show()
                        true
                    }R.id.menuChat -> {
                        var mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(order.idUser)
                        mDatabase.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    var img = p0.child("img").getValue(String::class.java).toString()
                                    onClick(order.nameUser.subSequence(12,order.nameUser.length).toString(),img,order.idUser)
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {}
                        })
                        true
                }
                    R.id.menuCancel -> {
                        var mDatabase = FirebaseDatabase.getInstance().getReference("FlowerOrder").child(order.idUser).child(order.id)
                        order.status = "Cancel"
                        mDatabase.setValue(order)
                        var message = "Your order " + order.id + " has been canceled !!"
                        sendMessage(order.idUser,message)
                        Toast.makeText(mContext, "Succesful", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> true
                }

            }
            popupMenu.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }

        public fun getIndexMessageFromFirebase(idUser : String) {
            var mDatabase = FirebaseDatabase.getInstance().getReference("ChatMessage").child("Admin").child(idUser)
            mDatabase.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        var max = 0
                        for (data in p0.children) {
                            var i = data.key?.toInt()
                            if (i?:0 > max){
                                max = (i?:0)
                            }
                        }
                        index = max + 1
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
        }
        private fun sendMessage(idUser : String,message:String) {
            var mDatabase =
                FirebaseDatabase.getInstance().getReference("ChatMessage").child("Admin")
                    .child(idUser)
            val img =
                "https://firebasestorage.googleapis.com/v0/b/wild-florist-d20.appspot.com/o/community.png?alt=media&token=1854368e-1732-440f-b283-29382e63d64c"
            var chat = Chat(index, "Admin", "Admin", img, message)
            mDatabase.child(index.toString()).setValue(chat)
            index += 1

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): listOrderViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListOrderBinding>(
            infler, R.layout.z_list_order, parent, false
        )
        return listOrderViewHolder(v)
    }

    override fun onBindViewHolder(holder: listOrderViewHolder, position: Int) {
        holder.v.order = listOrder[position]
        var order: Order = listOrder[position]
        holder.getIndexMessageFromFirebase(order.idUser)
        holder.mListFlowerCart = order.listFlower!!
        holder.mAdapter = ReceiptAdapter(holder.mListFlowerCart)
        holder.rView!!.layoutManager = LinearLayoutManager(context)
        holder.rView!!.setHasFixedSize(true)
        holder.rView!!.adapter = holder.mAdapter

        holder.layout!!.setOnClickListener {
            holder.popupMenus1(it,order)
        }

    }

    override fun getItemCount(): Int {
        return listOrder.size
    }
}