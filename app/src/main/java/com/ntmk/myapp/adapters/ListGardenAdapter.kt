package com.ntmk.myapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ntmk.myapp.R
import com.ntmk.myapp.model.ListGardenData

class ListGardenAdapter(private var Garden_List: MutableList<ListGardenData>) :
    RecyclerView.Adapter<ListGardenAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val LG_Name: TextView = itemView.findViewById(R.id.txtGarden_list)
        val LG_Temperature: TextView = itemView.findViewById(R.id.link_gardenTemp)
        val LG_Sun: TextView = itemView.findViewById(R.id.link_garden_sun)
        val LG_Weight: TextView = itemView.findViewById(R.id.link_garden_weight)
        val LG_Image: ImageView = itemView.findViewById(R.id.imgGarden_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.z_list_garden_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gardenData = Garden_List[position]
        holder.LG_Name.text = gardenData.ListG_name
        holder.LG_Temperature.text = gardenData.ListG_temperature
        holder.LG_Sun.text = gardenData.ListG_sun
        holder.LG_Weight.text = gardenData.ListG_weight
        holder.LG_Image.setImageResource(gardenData.ListG_image)
    }

    override fun getItemCount(): Int {
        return Garden_List.size
    }
}