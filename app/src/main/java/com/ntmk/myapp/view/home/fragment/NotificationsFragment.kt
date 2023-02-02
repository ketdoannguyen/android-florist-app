package com.ntmk.myapp.view.home.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.ListNotificationAdapter
import com.ntmk.myapp.databinding.FragmentNotificationsBinding
import com.ntmk.myapp.model.Notificationn

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var listNotification: ArrayList<Notificationn>
    private lateinit var mAdapter: ListNotificationAdapter
    private lateinit var mDatabase: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications, container, false)

        listNotification = ArrayList()

        mAdapter = ListNotificationAdapter(requireContext(), listNotification)

        binding.recyclerNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerNotification.setHasFixedSize(true)
        binding.recyclerNotification.adapter = mAdapter

        getFlowerData()

//        binding.imgCart.setOnClickListener {
//            val i = Intent(requireContext(), CartActivity::class.java)
//            startActivity(i)
//        }
        binding.imgMenu.setOnClickListener {
            popupMenus(it)
        }


        return binding.root
    }
    private fun popupMenus(v:View) {
        var popupMenu = PopupMenu(requireContext(), v)
//            popupMenu.menu.get(2).
        popupMenu.inflate(R.menu.main_menu_notification)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menuRead -> {
                    true
                }
                R.id.menuSetting -> {
                    true
                }

                else -> true
            }

        }
        popupMenu.show()
        val popup = PopupMenu::class.java.getDeclaredField("mPopup")
        popup.isAccessible = true
        val menu = popup.get(popupMenu)
        menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
            .invoke(menu,true)
    }
    fun getFlowerData() {
        mDatabase = FirebaseDatabase.getInstance().getReference("Notifications")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    listNotification.removeAll(listNotification)
                    for (data in p0.children) {
                        val notification = data.getValue(Notificationn::class.java)
//                        notification?.detail = notification?.detail + "\n" + notification?.time
                        listNotification.add(notification!!)
                    }

                    binding.recyclerNotification.adapter = mAdapter
                    binding.recyclerNotification.adapter?.notifyDataSetChanged()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Cancel", error.toString())
            }
        })

    }


}