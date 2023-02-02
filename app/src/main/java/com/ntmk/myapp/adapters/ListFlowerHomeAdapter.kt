package com.ntmk.myapp.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ntmk.myapp.databinding.ZListItemHomeViewBinding
import androidx.recyclerview.widget.RecyclerView
import com.ntmk.myapp.R
import com.ntmk.myapp.view.home.activity.ZViewerProductActivity
import com.ntmk.myapp.controller.CartFirebase
import com.ntmk.myapp.model.Flower
import com.ntmk.myapp.model.FlowerCart

class ListFlowerHomeAdapter(var context: Context, var flowerList: ArrayList<Flower>) :
    RecyclerView.Adapter<ListFlowerHomeAdapter.ListFlowerHomeViewHolder>() {
    var mContext: Context? = context

    inner class ListFlowerHomeViewHolder(var v: ZListItemHomeViewBinding) :
        RecyclerView.ViewHolder(v.root) {
        var itemFlower: RelativeLayout? = null
        var btnAddCart: ImageView? = null
        var database: CartFirebase = CartFirebase()

        init {
            itemFlower = v.lyHomeViewList
            btnAddCart = v.btnAddProductToCart
            database.getDataFlowerCart()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFlowerHomeViewHolder {
        val infler = LayoutInflater.from(parent.context)
        val v = DataBindingUtil.inflate<ZListItemHomeViewBinding>(
            infler, R.layout.z_list_item_home_view, parent, false
        )
        return ListFlowerHomeViewHolder(v)
    }

    override fun onBindViewHolder(holder: ListFlowerHomeViewHolder, position: Int) {
        holder.v.listItemHome = flowerList[position]
        var flower: Flower = flowerList[position]
        // này nó lấy cái đối tượng flower ra mà ta , nếu làm như m đó , thì cái id sửa lại thành như này
        holder.itemFlower?.setOnClickListener {
            onClickGoToDetail(flower)
        }

        holder.btnAddCart?.setOnClickListener {
            var listFlowerCart: ArrayList<FlowerCart> = ArrayList()
            listFlowerCart = holder.database.getListFlowerCart()
            var mFlowerCart: FlowerCart = FlowerCart()

            mFlowerCart.idFlower = flower.id!!
            mFlowerCart.name = flower.name!!
            mFlowerCart.price = flower.price!!
            mFlowerCart.img = flower.img!!
            var check: Boolean = false

            for (flower in listFlowerCart) {
                if (mFlowerCart.idFlower == flower.idFlower) {
                    mFlowerCart.id = flower.id
                    mFlowerCart.quantity = flower.quantity + 1
                    check = true
                }
            }
            if (!check) {
                if (listFlowerCart.size == 0) {
                    mFlowerCart.id = 0
                } else {
                    mFlowerCart.id = listFlowerCart.get(listFlowerCart.size - 1).id + 1
                }
                mFlowerCart.quantity = 1
            }

            holder.database.addFlowerCart(mFlowerCart)
            Toast.makeText(mContext, "Added to cart", Toast.LENGTH_LONG).show()
        }
    }

    private fun onClickGoToDetail(flower: Flower) {
        val i = Intent(mContext, ZViewerProductActivity::class.java)
        var mBundle: Bundle = Bundle()
        mBundle.putSerializable("Flower", flower)
        mBundle.putString("Activity","HomeActivity")
        i.putExtras(mBundle)
        mContext?.startActivity(i)

    }

    override fun getItemCount(): Int {
        return flowerList.size
    }
}