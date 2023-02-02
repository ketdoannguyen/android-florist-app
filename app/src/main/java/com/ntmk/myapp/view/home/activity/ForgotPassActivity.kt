package com.ntmk.myapp.view.home.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ntmk.myapp.R
import com.ntmk.myapp.databinding.ActivityForgotPassBinding

class ForgotPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_pass)

        binding.txtLinkSignup.setOnClickListener {
            val i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
        }
        binding.btnBack.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        binding.btnConfirm.setOnClickListener {
//            val v = View.inflate(this, R.layout.verify_email_fragment, null)
//
//            val builder = AlertDialog.Builder(this)
//            builder.setView(v)
//
//            val dialog = builder.create()
//            dialog.show()
//            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val email = binding.txtEmail.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(baseContext, "Email cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(baseContext, "Invalid Email", Toast.LENGTH_SHORT).show()
            } else {
                Firebase.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(baseContext, "Email sent , check your mailbox", Toast.LENGTH_SHORT).show()
                            val i = Intent(this, LoginActivity::class.java)
                            startActivity(i)
                        } else {
                            Toast.makeText(baseContext, "Email not registered !!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}