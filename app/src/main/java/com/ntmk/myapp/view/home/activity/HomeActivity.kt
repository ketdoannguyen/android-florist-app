package com.ntmk.myapp.view.home.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ActivityHomeBinding
import com.ntmk.myapp.view.home.fragment.*


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        binding.layoutControl.setOnClickListener {  }
        intentFragment(HomeFragment())
        binding.imgBottomHome.setImageResource(R.drawable.home)
        binding.imgBottomHome.setOnClickListener {
            BottomControlFirst()
            binding.imgBottomHome.setImageResource(R.drawable.home)
            intentFragment(HomeFragment())
        }
        binding.imgBottomGarden.setOnClickListener {
            BottomControlFirst()
            binding.imgBottomGarden.setImageResource(R.drawable.garden)
        }
        binding.imgBottomBlog.setOnClickListener {
            BottomControlFirst()
            binding.imgBottomBlog.setImageResource(R.drawable.blog_check)
            intentFragment(BlogFragment())
        }
        binding.imgBottomNotification.setOnClickListener {
            BottomControlFirst()
            binding.imgBottomNotification.setImageResource(R.drawable.notification)
            intentFragment(NotificationsFragment())
        }
        binding.imgBottomUser.setOnClickListener {
            BottomControlFirst()
            binding.imgBottomUser.setImageResource(R.drawable.user)
            intentFragment(ProfileFragment())
        }

        recevieDataOtherActivity()
    }

    private fun recevieDataOtherActivity(){
        var intent = intent
        var check : Boolean = intent.getBooleanExtra("OrderToProfile",false)
        if(check){
            BottomControlFirst()
            binding.imgBottomUser.setImageResource(R.drawable.user)
            intentFragment(ProfileFragment())
        }
    }

    public fun intentFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }


    private fun BottomControlFirst(){
        binding.imgBottomHome.setImageResource(R.drawable.home_unselect)
        binding.imgBottomGarden.setImageResource(R.drawable.gardening_unselect)
        binding.imgBottomBlog.setImageResource(R.drawable.blog)
        binding.imgBottomNotification.setImageResource(R.drawable.notification_unselected)
        binding.imgBottomUser.setImageResource(R.drawable.user_unselect)

    }
}