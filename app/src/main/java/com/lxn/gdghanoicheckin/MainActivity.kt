package com.lxn.gdghanoicheckin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<AppCompatButton>(R.id.button_create).setOnClickListener {
            val intent = Intent(this,CreateQRActivity::class.java)
            startActivity(intent)
        }
    }
}