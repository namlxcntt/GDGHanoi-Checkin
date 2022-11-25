package com.lxn.gdghanoicheckin.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.constant.DataState
import com.lxn.gdghanoicheckin.utils.logError
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator.addOverlayToCenter
import com.lxn.gdghanoicheckin.repository.SmsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQrViewModel @Inject constructor(
    private val repository: SmsRepository,
    private val application: Application
) : ViewModel() {

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
        viewModelScope.launch(Dispatchers.IO) {
            val listBitmap = ArrayList<Pair<String, Bitmap>>()
            val newList = arrayListOf<String>()
            newList.addAll(list)
            newList.removeAt(0)
            newList.forEach {
                delay(1000)
                val bitmap = BarcodeImageGenerator.generateBitmap(it, 4000, 4000)
                    .copy(Bitmap.Config.ARGB_8888, true)
                val bitmapCenter =
                    BitmapFactory.decodeResource(application.resources, R.drawable.bg_qr)
                        .copy(Bitmap.Config.ARGB_8888, true)
                val mergeBitmap = bitmapCenter.addOverlayToCenter(bitmap)
                uploadImageToFirebase((Pair(it, mergeBitmap)))
            }
            logError("Data Bitmap -> $listBitmap")
        }
    }

    private fun uploadImageToFirebase(data: Pair<String, Bitmap>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveImageAndPushSheet(data)
        }
    }
}