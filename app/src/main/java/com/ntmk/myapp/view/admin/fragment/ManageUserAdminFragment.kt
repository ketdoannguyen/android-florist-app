package com.ntmk.myapp.view.admin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ntmk.myapp.adapters.ListAdminUserAdapter
import com.ntmk.myapp.databinding.FragmentManageUserAdminBinding
import com.ntmk.myapp.model.User

class ManageUserAdminFragment : Fragment() {

    private lateinit var binding: FragmentManageUserAdminBinding

    private var rView: RecyclerView? = null
    private lateinit var listUser: ArrayList<User>
    private var mAdapter: ListAdminUserAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageUserAdminBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        binding.btnAdd.setOnClickListener { view -> showDialogUpdateProfile() }
        showUserAdmin()
        return root
    }

    private fun showUserAdmin() {
        rView = binding.listEmployerFollow
        listUser = ArrayList()
        val userAuth = FirebaseAuth.getInstance().currentUser
        val mDatabaseJobHeart = FirebaseDatabase.getInstance().getReference("Users")
        mDatabaseJobHeart.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshotJob: DataSnapshot) {
                listUser.removeAll(listUser)
                for (postSnapshotJob in dataSnapshotJob.children) {
                    listUser.add(postSnapshotJob.getValue(User::class.java)!!)
                    mAdapter = ListAdminUserAdapter(requireContext(), listUser)
                    val layout = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                    rView!!.setLayoutManager(layout)
                    rView!!.setAdapter(mAdapter)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}