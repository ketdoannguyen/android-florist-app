package com.ntmk.myapp.adapters

import android.content.Context
import android.util.Log
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ZListNotificationBinding
import com.ntmk.myapp.model.Notificationn


class ListNotificationAdapter(var context: Context, var listNotification: ArrayList<Notificationn>) :
    RecyclerView.Adapter<ListNotificationAdapter.ListNotificationViewHolder>() {
    var mContext: Context? = context

    inner class ListNotificationViewHolder(var v: ZListNotificationBinding) :
        RecyclerView.ViewHolder(v.root) {
        var txtTitle: TextView? = null
        var txtDetail: TextView? = null
        var txtTime: TextView? = null
        var layoutNotificationn: RelativeLayout? = null
        var cardView: CardView? = null
        var imgType: ImageView? = null
        var mNotification: Notificationn? = null
        var btnStatus: ImageButton? = null
        private lateinit var mDatabase: DatabaseReference


        init {
            txtTitle = v.txtTitle
            txtDetail = v.txtDetail
            txtTime = v.txtTime
            mNotification = v.notification
            layoutNotificationn = v.layoutNotification
            btnStatus = v.btnStatus
            cardView = v.cartView
            imgType = v.imgType
            layoutNotificationn!!.setOnClickListener { popupMenus(it) }
        }
        private fun popupMenus(v:View){
            var popupMenu = PopupMenu(mContext,v,Gravity.RIGHT)
//            popupMenu.menu.get(2).
            popupMenu.inflate(R.menu.show_menu_notification)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.menuDetail->{
                        Toast.makeText(mContext,"Details",Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menuRead->{
                        btnStatus?.setVisibility(View.GONE)
                        true
                    }
                    R.id.menuDelete->{
                        var listNotificationMenu = ArrayList<Notificationn>()
                        mDatabase = FirebaseDatabase.getInstance().getReference("Notifications")
                        mDatabase.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    for (data in p0.children) {
                                        val notification = data.getValue(Notificationn::class.java)
                                        listNotificationMenu.add(notification!!)
                                    }

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Cancel", error.toString())
                            }
                        })
                        var id = -1
                        for(notification in listNotificationMenu){
                            if(notification.time.equals(txtTime?.text.toString())){
                                id = notification.id!!
                            }
                        }
                        if(id == -1){
                            Toast.makeText(context,"Delete Failed", Toast.LENGTH_SHORT).show()
                        }else{
                            mDatabase = FirebaseDatabase.getInstance().getReference("Notifications/"+id)
                            mDatabase.removeValue()
                            Toast.makeText(context,"Delete Success", Toast.LENGTH_SHORT).show()
                        }
                        Toast.makeText(mContext,"Delete",Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menuReport->{
                        Toast.makeText(mContext,"Report this notification success",Toast.LENGTH_SHORT).show()
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNotificationViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListNotificationBinding>(
            infler, R.layout.z_list_notification, parent, false
        )
        return ListNotificationViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListNotificationViewHolder, position: Int) {
        holder.v.notification = listNotification[position]
        var mNotification: Notificationn = listNotification[position]
        if(mNotification.type.equals("Order")){
            holder.imgType?.setImageResource(R.drawable.imgorder);
        }else if(mNotification.type.equals("Voucher")){
            holder.imgType?.setImageResource(R.drawable.imgvoucher);
        }else if(mNotification.type.equals("Flower")){
            holder.imgType?.setImageResource(R.drawable.imgflower);
        }else if(mNotification.type.equals("Wallet")){
            holder.imgType?.setImageResource(R.drawable.imgwallet);
        }else if(mNotification.type.equals("User")){
            holder.imgType?.setImageResource(R.drawable.imguser);
        }else{
            holder.imgType?.setImageResource(R.drawable.imgquestion);
        }
        if(mNotification?.status.equals("unread")){
            holder.btnStatus?.setVisibility(View.VISIBLE)
        }else{
            holder.btnStatus?.setVisibility(View.GONE)
        }



    }


    override fun getItemCount(): Int {
        return listNotification.size
    }
}