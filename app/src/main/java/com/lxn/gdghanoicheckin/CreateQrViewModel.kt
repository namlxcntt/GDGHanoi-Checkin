package com.lxn.gdghanoicheckin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.constant.DataState
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator.addOverlayToCenter
import com.lxn.gdghanoicheckin.repository.SmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQrViewModel @Inject constructor(private val repository: SmsRepository, @ApplicationContext val context: Context) : ViewModel() {

    init {
    }

    fun getAllEmailFromSheet() {
        repository.getAllEmailFromSheet()
            .flowOn(Dispatchers.IO)
            .onEach {
                when (it) {
                    is DataState.Error -> {

                    }
                    DataState.Loading -> {
                        logError("Start get")
                    }
                    is DataState.Success -> {
                        val listData = it.data
                        generatedQr(listData)
                    }
                }
            }.catch { cause ->
                logError(cause)
            }
            .launchIn(viewModelScope)
    }

    private suspend fun generatedQr(list: List<String>) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            val listBitmap = ArrayList<Pair<String, Bitmap>>()
            list.forEach {
                val bitmap = BarcodeImageGenerator.generateBitmap(it, 1000, 1000).copy(Bitmap.Config.ARGB_8888,true)
                val bitmapCenter = BitmapFactory.decodeResource(context.resources,R.drawable.bg_png).copy(Bitmap.Config.ARGB_8888,true)
                val mergeBitmap = bitmapCenter.addOverlayToCenter(bitmap)
                uploadImageToFirebase((Pair(it, mergeBitmap)))
            }
            logError("Data Bitmap -> $listBitmap")
        }
    }

    private fun uploadImageToFirebase(data: Pair<String, Bitmap>) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            repository.saveImageAndPushSheet(data)
        }
    }
}