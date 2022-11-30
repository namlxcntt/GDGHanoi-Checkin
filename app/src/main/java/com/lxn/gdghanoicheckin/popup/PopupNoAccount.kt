package com.lxn.gdghanoicheckin.popup

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
import kotlinx.android.synthetic.main.dialog_edit_barcode_name.*

class PopupNoAccount(
    private val onClickApprove: () -> Unit,
) : DialogFragment() {

    companion object {
        private const val NAME_KEY = "NAME_KEY"

        fun newInstance(
            onClickApprove: () -> Unit,
        ): PopupNoAccount {
            return PopupNoAccount(onClickApprove)
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val dialog = context?.let { Dialog(it, R.style.AppThemeNew_DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_no_account)
        dialog?.show()

        val button = dialog?.findViewById<AppCompatButton>(R.id.btn_accept)

        button?.setOnClickListener {
            onClickApprove.invoke()
        }

        return dialog!!
    }


}