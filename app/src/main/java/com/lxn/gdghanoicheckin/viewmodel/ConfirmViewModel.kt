package com.lxn.gdghanoicheckin.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.constant.DataState
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
import com.lxn.gdghanoicheckin.network.model.SaveObject
import com.lxn.gdghanoicheckin.repository.SmsRepository
import com.lxn.gdghanoicheckin.utils.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ConfirmViewModel @Inject constructor(
    private val repository: SmsRepository,
    private val application: Application
) : ViewModel() {
    val dataCheckLive: MutableLiveData<Triple<Boolean, String, TypeCheckIn>> = MutableLiveData()

    fun getAllEmailFromSheetAndCheck(email: String) {
        repository.getEmailByCheck()
            .flowOn(Dispatchers.IO)
            .map {
                when (it) {
                    is DataState.Success -> {
                        val listData = it.data.first.subList(1, it.data.first.size)
                        val listChecked = it.data.second
                        listData.forEach { data ->
                            val emailApi = data.substringBefore("-")
                            if (emailApi == email.substringBefore("-").trim()) {
                                if (listChecked.contains(emailApi)) {
                                    return@map Triple(false, emailApi, TypeCheckIn.IsExited)
                                } else {
                                    repository.sendQRScanObject(
                                        saveObject = SaveObject(
                                            action = "saveScanned",
                                            "",
                                            from = emailApi
                                        )
                                    )
                                    if (data.contains(TypeCheckIn.Vip.value)) {
                                        return@map Triple(true, emailApi, TypeCheckIn.Vip)
                                    } else {
                                        return@map Triple(true, emailApi, TypeCheckIn.Normal)
                                    }
                                }
                            }
                        }
                        return@map Triple(false, email, TypeCheckIn.Normal)
                    }
                    else -> return@map Triple(false, email, TypeCheckIn.Normal)
                }
            }.onEach {
                dataCheckLive.value = it
            }.catch { cause ->
                logError(cause)
            }
            .launchIn(viewModelScope)
    }
}