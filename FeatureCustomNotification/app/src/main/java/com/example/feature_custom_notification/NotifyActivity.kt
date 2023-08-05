package com.example.feature_custom_notification

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NotifyActivity : AppCompatActivity() {
    lateinit var notifyText:TextView
    companion object {
        const val notify_title: String = "notify_title"
        const val notify_content: String = "notify_content"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify)
        updateUI(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        updateUI(intent)
        Log.d("TAG success", "onNewIntent: recieved")
    }

    private fun updateUI(intent: Intent?): Unit {
        val title = (intent?.extras?.get(notify_title)) as String?
        val content = intent?.extras?.get(notify_content) as String?
        notifyText = findViewById(R.id.notifyText)
        notifyText.text = title + "\n" + content
    }


}