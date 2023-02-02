package com.ntmk.myapp.view.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.ListBlogAdapter
import com.ntmk.myapp.databinding.FragmentBlogBinding
import com.ntmk.myapp.model.Blog


class BlogFragment : Fragment() {
    private lateinit var binding: FragmentBlogBinding

    private lateinit var blogList: ArrayList<Blog>
    private lateinit var mAdapter: ListBlogAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog, container, false)



//        Intent i = new Intent(getContext(),Blog_RSS.class);
//        startActivity(i);
        blogList = ArrayList()
        mAdapter = ListBlogAdapter(requireContext(),blogList)

        binding.listBlog.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.listBlog.setHasFixedSize(true)
        binding.listBlog.adapter = mAdapter
        showBlog()

        return binding.root
    }

    private fun showBlog() {
        val mDatabase = FirebaseDatabase.getInstance().getReference("Blog")
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                blogList.removeAll(blogList)
                for (postSnapshot in dataSnapshot.children) {
                    var blog = postSnapshot.getValue(Blog::class.java) as Blog
                    println(blog.toString())
                    blogList.add(blog)
                }
                binding.listBlog.adapter = mAdapter
                binding.listBlog.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

}