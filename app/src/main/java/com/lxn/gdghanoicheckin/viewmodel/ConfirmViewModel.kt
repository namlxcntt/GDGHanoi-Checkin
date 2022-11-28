package com.lxn.gdghanoicheckin.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.constant.DataState
import com.lxn.gdghanoicheckin.constant.TypeCheckIn
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
                        val listData = it.data.subList(1,it.data.size)
                        listData.forEach { data ->
                            val emailApi = data.substringBefore("-")
                            if (emailApi == email.substringBefore("-")) {
                                if (data.contains(TypeCheckIn.Vip.value)) {
                                    return@map Triple(true, emailApi, TypeCheckIn.Vip)
                                } else {
                                    return@map Triple(true, emailApi, TypeCheckIn.Normal)
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