package com.ntmk.myapp.view.home.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.FragmentProfileBinding
import com.ntmk.myapp.databinding.FragmentSettingBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.linkBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        binding.btbDeleteAcc.setOnClickListener {
            var userAuth = FirebaseAuth.getInstance().currentUser
            if (userAuth != null) {
                userAuth.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "User account deleted.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}