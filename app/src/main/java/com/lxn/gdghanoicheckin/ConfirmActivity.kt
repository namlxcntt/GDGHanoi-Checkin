package com.lxn.gdghanoicheckin

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity


class ConfirmActivity : AppCompatActivity() {
    companion object {
        private const val KEY = "KEY_TEXT"
        fun startActivity(context: Context, data: String) {
            val intent: Intent = Intent(context, ConfirmActivity::class.java)
            intent.putExtra(KEY, data)
            context.startActivity(intent)
        }
    }

    private val viewModel: CreateQrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        val text = intent.getStringExtra(KEY)

        viewModel.dataCheckLive.observe(this){

        }
        viewModel.getAllEmailFromSheetAndCheck(text ?: "")

    }

}