package com.ntmk.myapp.view.home.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ActivityBalanceBinding

class BalanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBalanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)
        binding = ActivityBalanceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.linkBack.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}