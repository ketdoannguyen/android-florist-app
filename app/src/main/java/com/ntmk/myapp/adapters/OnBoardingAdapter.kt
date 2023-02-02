package com.ntmk.myapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.ntmk.myapp.R
import com.ntmk.myapp.model.OnBoardingData

class OnBoardingAdapter(private var context : Context, private var onBoardingDataList: List<OnBoardingData>) : PagerAdapter() {
    override fun getCount(): Int {
        return onBoardingDataList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.slide_layout,null)

        val images: ImageView = view.findViewById(R.id.img_slide)
        val headings: TextView = view.findViewById(R.id.headingSlide)
        val titles: TextView = view.findViewById(R.id.title_slide)

        images.setImageResource(onBoardingDataList[position].images)
        headings.text = onBoardingDataList[position].headings
        titles.text = onBoardingDataList[position].titles

        container.addView(view)
        return view

    }
}