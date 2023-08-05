package com.example.feature_custom_notification

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class CustomNotifyActivity : AppCompatActivity() {
    lateinit var webview:WebView
    lateinit var progress:ProgressBar

    companion object {
        const val notify_title: String = "notify_title"
        const val notify_content: String = "notify_content"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_custom)

        webview = findViewById(R.id.webview)
        progress = findViewById(R.id.progress)
        webview.settings.javaScriptEnabled = true
        webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progress.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progress.visibility = View.GONE
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
        };

        loadNotificationPage(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        loadNotificationPage(intent)
    }

    private fun loadNotificationPage(intent: Intent?): Unit {
        intent?.dataString?.let { webview.loadUrl(it) };
    }


}