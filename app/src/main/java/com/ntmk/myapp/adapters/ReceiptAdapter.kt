package com.ntmk.myapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.ntmk.myapp.R
import com.ntmk.myapp.controller.CartFirebase
import com.ntmk.myapp.databinding.ZListCartProductBinding
import com.ntmk.myapp.databinding.ZListCartTabViewBinding
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FlowerCart

class ReceiptAdapter(var listFlowerCart: ArrayList<FlowerCart>) :
    RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder>() {

    inner class ReceiptViewHolder(var v: ZListCartProductBinding) :
        RecyclerView.ViewHolder(v.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListCartProductBinding>(
            infler, R.layout.z_list_cart_product, parent, false
        )
        return ReceiptViewHolder(v)
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {
        holder.v.receipt = listFlowerCart[position]
    }


    override fun getItemCount(): Int {
        return listFlowerCart.size
    }
}