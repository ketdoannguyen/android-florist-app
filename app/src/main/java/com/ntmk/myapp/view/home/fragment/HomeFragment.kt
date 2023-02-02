package com.ntmk.myapp.view.home.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.database.*
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.ListFlowerHomeAdapter
import com.ntmk.myapp.databinding.FragmentHomeBinding
import com.ntmk.myapp.model.Chat
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.view.home.activity.CartActivity
import com.ntmk.myapp.view.home.activity.ChatActivity
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    lateinit var mDatabase: DatabaseReference
    private lateinit var flowerFreshList: ArrayList<Flower>
    private lateinit var flowerDriedList: ArrayList<Flower>
    private lateinit var flowerArtificialList: ArrayList<Flower>
    private lateinit var flowerVasesList: ArrayList<Flower>

    private lateinit var mAdapter: ListFlowerHomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        flowerFreshList= ArrayList()
        flowerDriedList= ArrayList()
        flowerArtificialList= ArrayList()
        flowerVasesList = ArrayList()
        mAdapter = ListFlowerHomeAdapter(requireContext(), flowerFreshList)
        binding.listProduct.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        binding.listProduct.setHasFixedSize(true)
        binding.listProduct.adapter = mAdapter
        onClickCategories()


        binding.imgCart.setOnClickListener {
            val i = Intent(requireContext(), CartActivity::class.java)
            startActivity(i)
        }
        binding.imgMessage.setOnClickListener {
            val i = Intent(requireContext(), ChatActivity::class.java)
            startActivity(i)
        }
        binding.txtSearch.setOnClickListener {
            showDialogSearch()
        }

        return binding.root
    }


    fun getFlowerData() {
        flowerFreshList.clear()
        flowerDriedList.clear()
        flowerArtificialList.clear()
        flowerVasesList.clear()
        mDatabase = FirebaseDatabase.getInstance().getReference("Flowers")
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                var flower : Flower? = dataSnapshot.getValue(Flower::class.java)
                if(flower != null){
                    if(flower.categories.equals("FRESH")){
                        flowerFreshList.add(flower)
                    }else if(flower.categories.equals("DRIED")){
                        flowerDriedList.add(flower)
                    }else if(flower.categories.equals("ARTIFICIAL")){
                        flowerArtificialList.add(flower)
                    }else if(flower.categories.equals("VASES")){
                        flowerVasesList.add(flower)
                    }
                    mAdapter.notifyDataSetChanged()
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        mDatabase.addChildEventListener(childEventListener)
    }
    private fun showDialogSearch() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_search)
        val window = dialog.window ?: return
        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val windowAttribute = window.attributes
        windowAttribute.gravity = Gravity.TOP

//        EditText editText = dialog.findViewById(R.id.layout);
        dialog.show()
    }
    private fun onClickCategories(){
        CategoriesFirst()
        binding.txtCategories1.setTextColor(Color.parseColor("#993399"))
        binding.txtCategories1.setTypeface(binding.txtCategories1.getTypeface(), Typeface.BOLD);
        getFlowerData()
        binding.categoriesFresh.setOnClickListener {
            CategoriesFirst()
            binding.txtCategories1.setTextColor(Color.parseColor("#993399"))
            binding.txtCategories1.setTypeface(binding.txtCategories1.getTypeface(), Typeface.BOLD);

            mAdapter = ListFlowerHomeAdapter(requireContext(), flowerFreshList)
            binding.listProduct.setHasFixedSize(true)
            binding.listProduct.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        }
        binding.categoriesDried.setOnClickListener {
            CategoriesFirst()
            binding.txtCategories2.setTextColor(Color.parseColor("#993399"))
            binding.txtCategories2.setTypeface(binding.txtCategories2.getTypeface(), Typeface.BOLD);

            mAdapter = ListFlowerHomeAdapter(requireContext(), flowerDriedList)
            binding.listProduct.setHasFixedSize(true)
            binding.listProduct.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        }
        binding.categoriesArtificial.setOnClickListener {
            CategoriesFirst()
            binding.txtCategories3.setTextColor(Color.parseColor("#993399"))
            binding.txtCategories3.setTypeface(binding.txtCategories3.getTypeface(), Typeface.BOLD);

            mAdapter = ListFlowerHomeAdapter(requireContext(), flowerArtificialList)
            binding.listProduct.setHasFixedSize(true)
            binding.listProduct.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        }
        binding.categoriesVases.setOnClickListener {
            CategoriesFirst()
            binding.txtCategories4.setTextColor(Color.parseColor("#993399"))
            binding.txtCategories4.setTypeface(binding.txtCategories4.getTypeface(), Typeface.BOLD);

            mAdapter = ListFlowerHomeAdapter(requireContext(), flowerVasesList)
            binding.listProduct.setHasFixedSize(true)
            binding.listProduct.adapter = mAdapter
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun CategoriesFirst(){
        binding.txtCategories1.setTextColor(Color.parseColor("#888888"))
        binding.txtCategories2.setTextColor(Color.parseColor("#888888"))
        binding.txtCategories3.setTextColor(Color.parseColor("#888888"))
        binding.txtCategories4.setTextColor(Color.parseColor("#888888"))

        binding.txtCategories1.setTypeface(null, Typeface.NORMAL);
        binding.txtCategories2.setTypeface(null, Typeface.NORMAL);
        binding.txtCategories3.setTypeface(null, Typeface.NORMAL);
        binding.txtCategories4.setTypeface(null, Typeface.NORMAL);
    }

}