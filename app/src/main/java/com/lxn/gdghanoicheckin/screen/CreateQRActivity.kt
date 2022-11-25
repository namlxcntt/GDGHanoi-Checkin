package com.lxn.gdghanoicheckin.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.lxn.gdghanoicheckin.viewmodel.CreateQrViewModel
import com.lxn.gdghanoicheckin.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateQRActivity : AppCompatActivity() {
    private val viewModel : CreateQrViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_qractivity)
        viewModel.getAllEmailFromSheet()
    }
}