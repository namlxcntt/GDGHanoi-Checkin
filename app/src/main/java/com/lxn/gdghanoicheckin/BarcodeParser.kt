package com.lxn.gdghanoicheckin

import com.google.zxing.Result


object BarcodeParser {

    fun parseResult(result: Result): String {
        return result.text
    }

}