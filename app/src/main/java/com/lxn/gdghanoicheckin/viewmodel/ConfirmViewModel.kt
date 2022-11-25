package com.lxn.gdghanoicheckin.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lxn.gdghanoicheckin.constant.DataState
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
    val dataCheckLive: MutableLiveData<Pair<Boolean, String>> = MutableLiveData()

    fun getAllEmailFromSheetAndCheck(email: String) {
        repository.getAllEmailFromSheet()
            .flowOn(Dispatchers.IO)
            .map {
                when (it) {
                    is DataState.Success -> {
                        val listData = it.data
                        if (listData.contains(email)) {
                            return@map Pair(true, email)
                        }
                        return@map Pair(false, email)
                    }
                    else -> return@map Pair(false, email)
                }
            }.onEach {
                dataCheckLive.value = it
            }.catch { cause ->
                logError(cause)
            }
            .launchIn(viewModelScope)
    }
}