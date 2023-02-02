package com.ntmk.myapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ntmk.myapp.R
import com.ntmk.myapp.view.home.activity.WebBlogActivity
import com.ntmk.myapp.databinding.ZListBlogItemBinding
import com.ntmk.myapp.model.Blog

class ListBlogAdapter(var context: Context, var listBlog: ArrayList<Blog>) :
    RecyclerView.Adapter<ListBlogAdapter.ListBlogViewHolder>() {
    var mContext: Context? = context

    inner class ListBlogViewHolder(var v: ZListBlogItemBinding) :
        RecyclerView.ViewHolder(v.root) {
        var img: ImageView? = null
        var txt: TextView? = null
        var layout: LinearLayout? = null


        init {
            img = v.img
            txt= v.txtTitle
            layout = v.layoutBlog
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListBlogViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListBlogItemBinding>(
            infler, R.layout.z_list_blog_item, parent, false
        )
        return ListBlogViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListBlogViewHolder, position: Int) {
        holder.v.blog = listBlog[position]
        var blog = listBlog[position]

        holder.layout?.setOnClickListener(View.OnClickListener { view: View? ->
            val intent = Intent(mContext, WebBlogActivity::class.java)
            intent.putExtra("LinkNew",blog.link )
            mContext!!.startActivity(intent)
        })

    }

    override fun getItemCount(): Int {
        return listBlog.size
    }
}