package com.lxn.gdghanoicheckin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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