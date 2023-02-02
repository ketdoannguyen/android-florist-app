package com.ntmk.myapp.view.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.FragmentProfileBinding
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FollowFlower
import com.ntmk.myapp.model.User
import com.ntmk.myapp.view.home.activity.*
import com.squareup.picasso.Picasso

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var mFirebaseuser: FirebaseUser
    private var list_user: ArrayList<User> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        changeTextView()
        getCountMyLike()

//        mAuth = FirebaseAuth.getInstance()
//        mFirebaseuser = mAuth.currentUser!!
//        userFirebase.getData()


        //changeTextView()
        // Chuyển giao diện về fragment Setting
        val settingFragment = SettingFragment()
        binding.linkSetting.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(
                    R.id.fragment_container,
                    settingFragment,
                    SettingFragment::class.java.simpleName
                )
                    .addToBackStack(null).commit()
            }
        }
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            val i = Intent(requireContext(), LoginActivity().javaClass)
            startActivity(i)
        }

        // Chuyển giao diện về fragment Balance
        val balanceFragment = BalanceFragment()
//        binding.linkBalance.setOnClickListener {
//            fragmentManager?.beginTransaction()?.apply {
//                replace(R.id.nav_profile, balanceFragment, BalanceFragment::class.java.simpleName)
//                    .addToBackStack(null).commit()
//            }
//        }

        binding.layoutMyLike.setOnClickListener {
            val i = Intent(requireContext(), MyLikeActivity().javaClass)
            startActivity(i)
        }
        binding.layoutUpdateProfile.setOnClickListener {
            val i = Intent(requireContext(), UpdateProfileActivity().javaClass)
            startActivity(i)
        }
        binding.orderToPay.setOnClickListener {
            IntentToOrder("Confirm")
        }
        binding.orderToShip.setOnClickListener {
            IntentToOrder("Packing")
        }
        binding.orderReceive.setOnClickListener {
            IntentToOrder("Delivery")
        }
        binding.orderComplete.setOnClickListener {
            IntentToOrder("Complete")
        }
        binding.orderCancel.setOnClickListener {
            IntentToOrder("Cancel")
        }



        return binding.root
    }

    private fun IntentToOrder(status: String) {
        val i = Intent(requireContext(), OrderActivity().javaClass)
        i.putExtra("StatusOrder", status)
        startActivity(i)
    }

    private fun changeTextView() {
        //Show info
        mFirebaseuser = FirebaseAuth.getInstance().currentUser!!
        val nameAuth: String = mFirebaseuser.displayName.toString()
        val emailAuth: String = mFirebaseuser.email.toString()
        binding.tvNameAccount.setText(nameAuth)
        binding.tvEmail.setText(emailAuth)
        Picasso.get()
            .load(mFirebaseuser.photoUrl)
            .into(binding.img)

    }
    private fun getCountMyLike(){
        var userId = FirebaseAuth.getInstance().currentUser?.uid!!
        var mDatabase =
            FirebaseDatabase.getInstance().getReference("Users").child(userId).child("FollowFlower")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                var count : Int = Integer.parseInt(p0.childrenCount.toString())
                if(count <= 1  ){
                    binding.txtCountMylike.setText(count.toString() + " Like")
                }else{
                    binding.txtCountMylike.setText(count.toString() + " Likes")
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
