package com.ntmk.myapp.adapters

import android.content.Context
import android.content.IntentSender
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ZListChatAdminBinding
import com.ntmk.myapp.model.Chat
import com.ntmk.myapp.view.admin.fragment.ManageChatAdminFragment
import com.ntmk.myapp.view.home.fragment.SettingFragment

class ListChatAdminAdapter (var fragment : ManageChatAdminFragment, var context: Context, var listChat: ArrayList<Chat>) :
    RecyclerView.Adapter<ListChatAdminAdapter.ListChatAdminViewHolder>() {
    var mContext: Context? = context

    inner class ListChatAdminViewHolder(var v: ZListChatAdminBinding) :
        RecyclerView.ViewHolder(v.root) {
        var layout: RelativeLayout? = null

        init {
            layout = v.lyHomeViewList
        }
        fun onClick(name:String,imgSender: String,idSender: String){
            fragment.onClickLayoutChat(name,imgSender,idSender)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListChatAdminViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListChatAdminBinding>(
            infler, R.layout.z_list_chat_admin, parent, false
        )
        return ListChatAdminViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListChatAdminViewHolder, position: Int) {
        holder.v.chat = listChat[position]
        var chat : Chat = listChat[position]

        holder.layout!!.setOnClickListener {holder.onClick(chat.nameSender,chat.imgSender,chat.idSender) }
    }


    override fun getItemCount(): Int {
        return listChat.size
    }
}