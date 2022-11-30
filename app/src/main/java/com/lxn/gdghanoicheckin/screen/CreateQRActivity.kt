package com.lxn.gdghanoicheckin.screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.lxn.gdghanoicheckin.viewmodel.CreateQrViewModel
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.popup.EditBarcodeNameDialogFragment
import com.lxn.gdghanoicheckin.popup.PopupRequirePassword
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create_qractivity.*

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
        PopupRequirePassword.newInstance {
            viewModel.getAllEmailFromSheet()
            viewModel.uploadState.observe(this) {

            }
        }.show(supportFragmentManager,"Hello")

    }
}