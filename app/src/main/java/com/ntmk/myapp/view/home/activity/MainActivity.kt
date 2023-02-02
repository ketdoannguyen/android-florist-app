package com.ntmk.myapp.view.home.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ActivityMainBinding
import com.ntmk.myapp.view.admin.AdminActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        Handler().postDelayed({
            k@
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish()
        }, 2000)
    }
}