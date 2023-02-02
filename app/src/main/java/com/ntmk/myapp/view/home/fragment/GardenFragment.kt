package com.ntmk.myapp.view.home.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.FragmentGardenBinding
import com.ntmk.myapp.adapters.ListGardenAdapter
import com.ntmk.myapp.model.ListGardenData

class GardenFragment : Fragment() {

    private lateinit var binding: FragmentGardenBinding

    //    add data list
    private var Garden_data = mutableListOf<ListGardenData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_garden, container, false)

        postToListHome()

//      RecycleView
        val rvGardenList: RecyclerView = binding.listGarden

//        layout
        rvGardenList.layoutManager =
            GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        rvGardenList.adapter = ListGardenAdapter(Garden_data)

        return binding.root
    }

    //    list item garden
    private fun postToListHome() {
        Garden_data.add(
            ListGardenData(
                "Name Flower 1",
                "10°C",
                "98%",
                "15%",
                R.drawable.list_flower
            )
        )
        Garden_data.add(
            ListGardenData(
                "Name Flower 2",
                "20°C",
                "78%",
                "50%",
                R.drawable.list_flower
            )
        )
        Garden_data.add(
            ListGardenData(
                "Name Flower 3",
                "20°C",
                "80%",
                "27%",
                R.drawable.list_flower
            )
        )
    }
}