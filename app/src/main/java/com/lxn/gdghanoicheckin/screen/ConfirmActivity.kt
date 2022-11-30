package com.lxn.gdghanoicheckin.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
import com.lxn.gdghanoicheckin.popup.EditBarcodeNameDialogFragment
import com.lxn.gdghanoicheckin.popup.PopupNoAccount
import com.lxn.gdghanoicheckin.viewmodel.ConfirmViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_confirm.*


@AndroidEntryPoint
class ConfirmActivity : AppCompatActivity() {
    companion object {
        private const val KEY = "KEY_TEXT"
        fun startActivity(context: Context, data: String) {
            val intent: Intent = Intent(context, ConfirmActivity::class.java)
            intent.putExtra(KEY, data)
            context.startActivity(intent)
        }
    }

    private val viewModel: ConfirmViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)
        val text = intent.getStringExtra(KEY)
        showLoading()
        observeData()
        viewModel.getAllEmailFromSheetAndCheck(text ?: "")

    }

    private fun observeData() {
        viewModel.dataCheckLive.observe(this) {
            loadingView.isVisible = false
            if (it.first) {
                showPopUpDialog(it.second, it.third)
            } else {
                showPopupError()
            }
        }
    }

    private fun showLoading() {
        loadingView.isVisible = true
    }

    private fun showPopupError() {
        val errorDialog = PopupNoAccount.newInstance(::finish)
        errorDialog.show(supportFragmentManager, "")
    }


    private fun showPopUpDialog(name: String, type: TypeCheckIn) {
        val errorDialog =
            EditBarcodeNameDialogFragment.newInstance(
                name = name,
                ::finish,
                typeCheckIn = type
            )
        errorDialog.show(supportFragmentManager, "")
    }

}