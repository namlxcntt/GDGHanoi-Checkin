package com.lxn.gdghanoicheckin.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CreateQrViewModel @Inject constructor(
    private val repository: SmsRepository,
    private val application: Application
) : ViewModel() {

    private val _uploadState: MutableLiveData<Boolean> = MutableLiveData(false)

    val uploadState: LiveData<Boolean> get() = _uploadState


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

    @OptIn(FlowPreview::class)
    private fun generatedQr(list: DataResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val listMerge: ArrayList<String> = ArrayList()
            withContext(Dispatchers.IO) {
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
            }

            val newList = arrayListOf<String>()
            newList.addAll(listMerge)
            newList.removeAt(0)

            newList.forEach { data ->
                logError("Data new -> $data")
                val bitmap = BarcodeImageGenerator.scaleBitmap(
                    BarcodeImageGenerator.generateBitmap(
                        data,
                        1792,
                        1792,
                    ).copy(Bitmap.Config.ARGB_8888, true),
                    3584f, 3584f,
                )
                bitmap?.let { bit ->
                    val bitmapCenter =
                        BitmapFactory.decodeResource(application.resources, R.drawable.resize_bg_qr)
                            .copy(Bitmap.Config.ARGB_8888, true)
                    val mergeBitmap = bitmapCenter.addOverlayToCenter(bit)
                    bitmapCenter?.let {
                        repository.awaitSinglePushValue(Pair(data, mergeBitmap))
                            .flowOn(Dispatchers.IO)
                            .flatMapConcat {
                                return@flatMapConcat repository.sendQrContent(it)
                            }
                            .flowOn(Dispatchers.IO)
                            .onEach {
                                Log.e("Namlxcntt","Push Data Success")
                            }
                            .catch {
                                Log.e("Namlxcntt", "Error ${it.message}")
                            }.launchIn(viewModelScope)
                    }
                }
                delay(5000L)
            }
        }
    }

}