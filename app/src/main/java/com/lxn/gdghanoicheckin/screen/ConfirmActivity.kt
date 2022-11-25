package com.lxn.gdghanoicheckin.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.popup.EditBarcodeNameDialogFragment
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
        loadingView.isVisible = true
        viewModel.dataCheckLive.observe(this) {
            loadingView.isVisible = false
            if (it.first) {
                showPopUpDialog(it.second)
            }
        }
        viewModel.getAllEmailFromSheetAndCheck(text ?: "")

    }

    fun showLoading() {

    }

    private fun onClickApprove() {
        finish()
    }

    private fun showPopUpDialog(name: String) {
        val errorDialog =
            EditBarcodeNameDialogFragment.newInstance(
                name = name,
                ::onClickApprove
            )
        errorDialog.show(supportFragmentManager, "")
    }

}