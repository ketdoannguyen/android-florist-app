package com.ntmk.myapp.view.home.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ntmk.myapp.R
import com.ntmk.myapp.adapters.OnBoardingAdapter
import com.ntmk.myapp.model.OnBoardingData

class AdvertisementActivity : AppCompatActivity() {

    //    add init
    private var onBoardingAdapter: OnBoardingAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onBoardingViewPaper: ViewPager? = null
    var btn_next: Button? = null
    var btn_skip: Button? = null
    var position = 0
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        setContentView(R.layout.activity_advertisement)


        if(restorePrefData()){
            gotoLogin()
            finish()
        }

        tabLayout = findViewById(R.id.tab)
        btn_next = findViewById(R.id.btn_next)
        btn_skip = findViewById(R.id.btn_skip)

        // add data list
        val onBoardingData:MutableList<OnBoardingData> = ArrayList()
        onBoardingData.add(OnBoardingData("Flower","Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            R.drawable.sl1
        ))
        onBoardingData.add(OnBoardingData("Payment","Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            R.drawable.sl2
        ))
        onBoardingData.add(OnBoardingData("Delivery","Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.",
            R.drawable.sl3
        ))

        setViewPaperAdapter(onBoardingData)

        // add onClickListener for btn_bottom
        position = onBoardingViewPaper!!.currentItem
        btn_next?.setOnClickListener{

            if(position<onBoardingData.size){
                position++
                onBoardingViewPaper!!.currentItem = position
            }
            if(position == onBoardingData.size){
                savePreData()
                gotoLogin()
            }
        }
        btn_skip?.setOnClickListener{
            gotoLogin()
        }

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

                position = tab!!.position
                if(tab.position == onBoardingData.size - 1){
                    btn_next!!.text = "FINISH"
//                    btn_next?.setOnClickListener{
//                        startActivity(Intent(activity,LoginActivity::class.java))
//                        finish()
//                    }
                }else{
                    btn_next!!.text = "NEXT"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setViewPaperAdapter(onBoardingData: List<OnBoardingData>){
        onBoardingViewPaper = findViewById(R.id.SlideVIewPaper)
        onBoardingAdapter = OnBoardingAdapter(this, onBoardingData)
        onBoardingViewPaper!!.adapter = onBoardingAdapter
        tabLayout?.setupWithViewPager(onBoardingViewPaper)
    }

    private fun gotoLogin(){
        val i= Intent(applicationContext, LoginActivity::class.java)
        startActivity(i)
    }

    private fun savePreData(){
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor:SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun",true)
        editor.apply()
    }

    private fun restorePrefData(): Boolean{
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)
    }
}