package com.lxn.gdghanoicheckin.repository

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.lxn.gdghanoicheckin.constant.DataState
import com.lxn.gdghanoicheckin.logError
import com.lxn.gdghanoicheckin.network.model.SaveObject
import com.lxn.gdghanoicheckin.network.retrofit.ApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*


/**
 * @author Namlxcntt
 * Create on 18/07/2021
 * Contact me: namlxcntt@gmail.com
 */
class SmsRepository(
    private val smsRetrofit: ApiService,
    private val firebaseStorage: FirebaseStorage
) {
    private var checkSms: Int = 0

    fun getAllEmailFromSheet() = flow {
        val response = smsRetrofit.getAllEmailFromSheet()
        try {
            emit(DataState.Success(response))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    suspend fun sendSms(data: SaveObject) {
//        try {
//            val smsMatch = smsRetrofit.sendQrContent(data)
//            sendDataToFirebase(data)
//        } catch (exception: Exception) {
//            if (checkSms == 0) {
//                sendSmsPhoneNumber(Constant.PHONE_NUMBER, Constant.MESSAGE_NO_INTERNET)
//                checkSms += 1
//            }
//            sendSmsPhoneNumber(Constant.PHONE_NUMBER, "From : ${data.from} \n ${data.content}")
//        }
    }

    private fun getDate(milliSeconds: Long): String? {
        val formatter = SimpleDateFormat("dd-MM-yyyy hh-mm-ss-SSS", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun saveImageAndPushSheet(data: Pair<String, Bitmap>) {
        val storageRef = firebaseStorage.reference.root.child("QRCode").child(data.first)
        val baos = ByteArrayOutputStream()
        data.second.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val dataByteArray = baos.toByteArray()
        val uploadTask = storageRef.putBytes(dataByteArray)
        uploadTask.addOnFailureListener {
            logError("Upload image failure")
        }
        uploadTask.addOnSuccessListener { snapshot ->
            storageRef.downloadUrl.addOnSuccessListener {
                val saveObject = SaveObject(
                    action = "save",
                    content = it.toString(),
                    from = data.first
                )
                sendQrContent(saveObject)
                logError("Upload image Success")
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun sendQrContent(saveObject: SaveObject) {
        GlobalScope.launch(Dispatchers.IO) {
            smsRetrofit.sendQrContent(saveObject)
            logError("Push To sheet")
        }
    }

}