package com.ntmk.myapp.view.home.activity

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.ntmk.myapp.databinding.ActivityWebBlogBinding

class WebBlogActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBlogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBlogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        val link = intent.getStringExtra("LinkNew")
        binding.webview.loadUrl(link.toString())
        binding.webview.setWebViewClient(WebViewClient())
    }
}