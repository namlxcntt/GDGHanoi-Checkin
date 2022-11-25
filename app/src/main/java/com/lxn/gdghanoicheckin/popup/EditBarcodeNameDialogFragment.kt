package com.lxn.gdghanoicheckin.popup

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import com.lxn.gdghanoicheckin.R

class EditBarcodeNameDialogFragment(val onClickApprove: () -> Unit) : DialogFragment() {

    companion object {
        private const val NAME_KEY = "NAME_KEY"

        fun newInstance(name: String?, onClickApprove: () -> Unit): EditBarcodeNameDialogFragment {
            return EditBarcodeNameDialogFragment(onClickApprove).apply {
                arguments = Bundle().apply {
                    putString(NAME_KEY, name)
                }
            }
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val name = arguments?.getString(NAME_KEY).orEmpty()

        val dialog = context?.let { Dialog(it, R.style.AppThemeNew_DialogTheme) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_edit_barcode_name)
        dialog?.show()

        val content = dialog?.findViewById<AppCompatTextView>(R.id.tv_content)

        content?.text = name

        val button = dialog?.findViewById<AppCompatButton>(R.id.btn_accept)

        button?.setOnClickListener {
            onClickApprove.invoke()
        }

//        val txt = dialog?.findViewById<EditText>(R.id.edit_text_barcode_name)
//
//        if (txt != null) {
//        }
//
//        val yesBt = dialog?.findViewById<TextView>(R.id.tv_accept)
//        yesBt?.setOnClickListener {
//            listener?.onNameConfirmed(txt?.text.toString())
//            dialog.dismiss()
//        }
//
//        val cancelBt = dialog?.findViewById<TextView>(R.id.tv_cancel)
//        cancelBt?.setOnClickListener {
//            dialog.dismiss()
//        }
        return dialog!!
    }


}