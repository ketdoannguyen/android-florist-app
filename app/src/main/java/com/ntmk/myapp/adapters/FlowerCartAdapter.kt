package com.ntmk.myapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ntmk.myapp.R
import com.ntmk.myapp.controller.CartFirebase
import com.ntmk.myapp.databinding.ZListCartTabViewBinding
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FlowerCart

class FlowerCartAdapter(var context: Context, var listFlowerCart: ArrayList<FlowerCart>) :
    RecyclerView.Adapter<FlowerCartAdapter.ListFlowerCartViewHolder>() {
    var mContext: Context? = context

    inner class ListFlowerCartViewHolder(var v: ZListCartTabViewBinding) :
        RecyclerView.ViewHolder(v.root) {
        var btnRemoveFlower: ImageView? = null
        var txtQuantity: EditText? = null
        var btnIncreaseQuantity: ImageButton? = null
        var btnDecreaseQuantity: ImageButton? = null
//        var checkbox: CheckBox? = null
        var database: CartFirebase = CartFirebase()

        init {
            txtQuantity = v.txtQuantity
            btnRemoveFlower = v.btnRemoveFlower
            btnIncreaseQuantity = v.btnIncreaseqQuantity
            btnDecreaseQuantity = v.btnDecreaseqQuantity
            database.getDataFlowerCart()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFlowerCartViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListCartTabViewBinding>(
            infler, R.layout.z_list_cart_tab_view, parent, false
        )
        return ListFlowerCartViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListFlowerCartViewHolder, position: Int) {
        holder.v.flowerCart = listFlowerCart[position]
        var flower: FlowerCart = listFlowerCart[position]
        holder.btnIncreaseQuantity?.setOnClickListener {
            holder.txtQuantity?.setText(
                (holder.txtQuantity?.text.toString().toInt() + 1).toString()
            )
            flower.quantity = holder.txtQuantity?.text.toString().toInt()
            holder.database.addFlowerCart(flower)
            Toast.makeText(mContext, "Cart updated", Toast.LENGTH_SHORT).show()
        }

        holder.btnDecreaseQuantity?.setOnClickListener {
            if (holder.txtQuantity?.text.toString().toInt() > 1) {
                holder.txtQuantity?.setText(
                    (holder.txtQuantity?.text.toString().toInt() - 1).toString()
                )
            } else {
                holder.txtQuantity?.setText("1")
            }
            flower.quantity = holder.txtQuantity?.text.toString().toInt()
            holder.database.addFlowerCart(flower)
            Toast.makeText(mContext, "Cart updated", Toast.LENGTH_SHORT).show()
        }

        holder.btnRemoveFlower?.setOnClickListener {
            var userId = FirebaseAuth.getInstance().currentUser?.uid!!
            var database =
                FirebaseDatabase.getInstance().getReference("FlowerCart").child(userId).child(flower.id.toString())
            database.removeValue()
            Toast.makeText(mContext, "Cart updated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return listFlowerCart.size
    }
}