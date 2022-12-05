package com.lxn.gdghanoicheckin.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.popup.PopupRequirePassword
import com.lxn.gdghanoicheckin.viewmodel.CreateQrViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateQRActivity : AppCompatActivity() {

    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, CreateQRActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel: CreateQrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_qractivity)
        val popupRequirePassword = PopupRequirePassword.newInstance {
            viewModel.getAllEmailFromSheet()
            viewModel.uploadState.observe(this) {
            }
        }
        popupRequirePassword.isCancelable = false
        popupRequirePassword.show(supportFragmentManager, "Hello")

    }

}