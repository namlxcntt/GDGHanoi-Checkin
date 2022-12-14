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
               val message = if (it.third == TypeCheckIn.IsExited){
                   "Tài khoản đã tồn tại"
                }else{
                  "Rất tiếc, chúng tôi không tìm thấy tài khoản này"
                }
                showPopupError(message)
            }
        }
    }

    private fun showLoading() {
        loadingView.isVisible = true
    }

    private fun showPopupError(message : String) {
        val errorDialog = PopupNoAccount.newInstance(message, ::finish)
        errorDialog.show(supportFragmentManager, "")
    }


    private fun showPopUpDialog(name: String, type: TypeCheckIn) {
        val errorDialog =
            EditBarcodeNameDialogFragment.newInstance(
                name = name,
                ::finish,
                typeCheckIn = type
            )
        errorDialog.isCancelable = false
        errorDialog.show(supportFragmentManager, "")
    }

}