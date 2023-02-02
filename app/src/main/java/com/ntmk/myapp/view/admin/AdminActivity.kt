package com.ntmk.myapp.view.admin

import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ActivityAdminBinding
import com.ntmk.myapp.view.admin.fragment.*

class AdminActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityAdminBinding

    private val FRAGMENT_HOME = 0
    private val FRAGMENT_JOB = 1
    private val FRAGMENT_EMPLOYER = 2
    private val FRAGMENT_BLOG = 3
    private val FRAGMENT_USER = 4
    private val FRAGMENT_CHAT = 5

    private var mCurrentFragment = FRAGMENT_HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_view_open,
            R.string.navigation_view_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.setNavigationItemSelectedListener(this)

        val i = intent
        val check = i.getStringExtra("AddEmployer")
        if (check != null && check == "true") {
            replaceFragment(HomeAdminFragment())
            mCurrentFragment = FRAGMENT_EMPLOYER
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.nav_home_admin) {
            if (mCurrentFragment != FRAGMENT_HOME) {
                replaceFragment(HomeAdminFragment())
                mCurrentFragment = FRAGMENT_HOME
            }
        } else if (id == R.id.nav_product) {
            if (mCurrentFragment != FRAGMENT_JOB) {
                replaceFragment(ManageProductAdminFragment())
                mCurrentFragment = FRAGMENT_JOB
            }
        } else if (id == R.id.nav_order) {
            if (mCurrentFragment != FRAGMENT_EMPLOYER) {
                replaceFragment(ManageOrderAdminFragment())
                mCurrentFragment = FRAGMENT_EMPLOYER
            }
        } else if (id == R.id.nav_blog) {
            if (mCurrentFragment != FRAGMENT_BLOG) {
                replaceFragment(ManageBlogAdminFragment())
                mCurrentFragment = FRAGMENT_BLOG
            }
        } else if (id == R.id.nav_user) {
            if (mCurrentFragment != FRAGMENT_USER) {
                replaceFragment(ManageUserAdminFragment())
                mCurrentFragment = FRAGMENT_USER
            }
        }else if (id == R.id.nav_chat) {
            if (mCurrentFragment != FRAGMENT_CHAT) {
                replaceFragment(ManageChatAdminFragment())
                mCurrentFragment = FRAGMENT_CHAT
            }
        }else if (id == R.id.nav_logout) {
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceFragment(mFragment: Fragment?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.context_fragment, mFragment!!)
        transaction.commit()
    }
}