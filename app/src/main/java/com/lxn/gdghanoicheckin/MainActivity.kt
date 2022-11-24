package com.lxn.gdghanoicheckin

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val PERMISSION_REQUEST_CODE = 101
        private const val ZXING_SCAN_INTENT_ACTION = "com.google.zxing.client.android.SCAN"
        private const val CONTINUOUS_SCANNING_PREVIEW_DELAY = 500L
    }

    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initScanner()
        requestPermissions()
//        findViewById<AppCompatButton>(R.id.button_create).setOnClickListener {
//            val intent = Intent(this,CreateQRActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun requestPermissions() {
        PermissionsHelper.requestNotGrantedPermissions(
            this,
            PERMISSIONS,
            PERMISSION_REQUEST_CODE
        )
    }

    private fun areAllPermissionsGranted(): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(this, PERMISSIONS)
    }

    private fun areAllPermissionsGranted(grantResults: IntArray): Boolean {
        return PermissionsHelper.areAllPermissionsGranted(grantResults)
    }

    private fun initScanner() {
        codeScanner = CodeScanner(this, scanner_view).apply {
            camera = CodeScanner.CAMERA_BACK
            autoFocusMode = AutoFocusMode.CONTINUOUS
            formats = listOf(BarcodeFormat.QR_CODE)
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isTouchFocusEnabled = false
            decodeCallback = DecodeCallback(::handleScannedBarcode)
            errorCallback = ErrorCallback(::showError)
        }
    }

    override fun onResume() {
        super.onResume()
        if (areAllPermissionsGranted()) {
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }

    private fun handleScannedBarcode(result: Result) {
        val barcode = BarcodeParser.parseResult(result)
        logError(barcode)
    }

    private fun showError(error: Throwable?) {
        logError(error?.message.toString())
    }


}