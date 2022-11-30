package com.lxn.gdghanoicheckin.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.R
import com.lxn.gdghanoicheckin.constant.DataState
import com.lxn.gdghanoicheckin.network.model.DataResponse
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator
import com.lxn.gdghanoicheckin.qr.BarcodeImageGenerator.addOverlayToCenter
import com.lxn.gdghanoicheckin.repository.SmsRepository
import com.lxn.gdghanoicheckin.utils.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateQrViewModel @Inject constructor(
    private val repository: SmsRepository,
    private val application: Application
) : ViewModel() {

    private val _uploadState : MutableLiveData<Boolean> = MutableLiveData(false)

    val uploadState : LiveData<Boolean> get() = _uploadState


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

    private fun generatedQr(list: DataResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val listMerge: ArrayList<String> = ArrayList()
            list.forEach { dataResponse ->
                val stringBuilder = StringBuilder()
                dataResponse.forEachIndexed { index, subData ->
                    if (index != 0) {
                        stringBuilder.append("-$subData")
                    } else {
                        stringBuilder.append(subData)
                    }
                }
                listMerge.add(stringBuilder.toString())
            }

            val newList = arrayListOf<String>()
            newList.addAll(listMerge)
            newList.removeAt(0)
            newList.forEach {
                logError("Data new -> $it")
                delay(1000)
                val bitmap = BarcodeImageGenerator.generateBitmap(it, 5000, 5000).copy(Bitmap.Config.ARGB_8888, true)
                val bitmapCenter = BitmapFactory.decodeResource(application.resources, R.drawable.bg_qr).copy(Bitmap.Config.ARGB_8888, true)
                val mergeBitmap = bitmapCenter.addOverlayToCenter(bitmap)
                uploadImageToFirebase((Pair(it, mergeBitmap)))
            }
        }
    }

    private suspend fun uploadImageToFirebase(data: Pair<String, Bitmap>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveImageAndPushSheet(data)
            withContext(Dispatchers.Main){
                _uploadState.value = true
            }
        }

    }
}