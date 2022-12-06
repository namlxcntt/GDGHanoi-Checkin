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

class EditBarcodeNameDialogFragment(
    private val onClickApprove: () -> Unit,
    private val typeCheckIn: TypeCheckIn
) : DialogFragment() {

    companion object {
        private const val NAME_KEY = "NAME_KEY"

        fun newInstance(
            name: String?,
            onClickApprove: () -> Unit,
            typeCheckIn: TypeCheckIn
        ): EditBarcodeNameDialogFragment {
            return EditBarcodeNameDialogFragment(onClickApprove, typeCheckIn).apply {
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
        val tvType = dialog?.findViewById<AppCompatTextView>(R.id.tv_type)

        content?.text = name

        tvType?.text = getString(
            R.string.content_role,
            if (typeCheckIn == TypeCheckIn.Vip) "Khách VIP" else "Người tham dự"
        )

        val button = dialog?.findViewById<AppCompatButton>(R.id.btn_accept)

        button?.setOnClickListener {
            onClickApprove.invoke()
        }

        return dialog!!
    }


}