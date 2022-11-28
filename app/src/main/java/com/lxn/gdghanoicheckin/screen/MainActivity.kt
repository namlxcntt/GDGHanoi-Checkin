package com.lxn.gdghanoicheckin.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.lxn.gdghanoicheckin.utils.PermissionsHelper
import com.lxn.gdghanoicheckin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        requestPermissions()
        btn_checkin.setOnClickListener {
            ScanQRActivity.startActivity(this)
        }
        btn_create_qr.setOnClickListener {
            CreateQRActivity.startActivity(this)
        }
    }

    private fun requestPermissions() {
        PermissionsHelper.requestNotGrantedPermissions(
            this,
            ScanQRActivity.PERMISSIONS,
            ScanQRActivity.PERMISSION_REQUEST_CODE
        )
    }

    private fun areAllPermissionsGranted(): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(this, ScanQRActivity.PERMISSIONS)
    }

    private fun areAllPermissionsGranted(grantResults: IntArray): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(grantResults)
    }


}