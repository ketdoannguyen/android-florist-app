package com.ntmk.myapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.ntmk.myapp.databinding.ZListFollowFlowerBinding
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FlowerCart
import com.ntmk.myapp.view.home.activity.ZViewerProductActivity

class FollowFlowerAdapter(var context: Context, var listFlower: ArrayList<Flower>) :
    RecyclerView.Adapter<FollowFlowerAdapter.ListFollowFlowerViewHolder>() {
    var mContext: Context? = context

    inner class ListFollowFlowerViewHolder(var v: ZListFollowFlowerBinding) :
        RecyclerView.ViewHolder(v.root) {
        var btnHearth: ImageView? = null
        var layout : RelativeLayout? = null

        init {
            btnHearth = v.btnHeart
            layout = v.layoutFollowFlower
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFollowFlowerViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListFollowFlowerBinding>(
            infler, R.layout.z_list_follow_flower, parent, false
        )
        return ListFollowFlowerViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListFollowFlowerViewHolder, position: Int) {
        holder.v.flower = listFlower[position]
        var flower: Flower = listFlower[position]

        holder.btnHearth?.setOnClickListener {
            var userId = FirebaseAuth.getInstance().currentUser?.uid!!
            var mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FollowFlower")
            mDatabase.child(flower.id).removeValue()
            listFlower.remove(flower)
            Toast.makeText(mContext, "Removed to My Likes", Toast.LENGTH_SHORT).show()
        }
        holder.layout?.setOnClickListener { onClickGoToDetail(flower) }
    }

    override fun getItemCount(): Int {
        return listFlower.size
    }
    private fun onClickGoToDetail(flower: Flower) {
        val i = Intent(mContext, ZViewerProductActivity::class.java)
        var mBundle: Bundle = Bundle()
        mBundle.putSerializable("Flower", flower)
        mBundle.putString("Activity","MyLikeActivity")
        i.putExtras(mBundle)
        mContext?.startActivity(i)

    }
}