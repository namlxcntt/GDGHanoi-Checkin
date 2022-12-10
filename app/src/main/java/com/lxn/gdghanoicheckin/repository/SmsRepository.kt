package com.lxn.gdghanoicheckin.repository

import android.graphics.Bitmap
import android.net.Uri
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.lxn.gdghanoicheckin.constant.DataState
import com.lxn.gdghanoicheckin.network.model.SaveObject
import com.lxn.gdghanoicheckin.network.retrofit.ApiService
import com.lxn.gdghanoicheckin.utils.logError
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import java.io.ByteArrayOutputStream


/**
 * @author Namlxcntt
 * Create on 18/07/2021
 * Contact me: namlxcntt@gmail.com
 */
class SmsRepository(
    private val smsRetrofit: ApiService,
    private val firebaseStorage: FirebaseStorage
) {

    fun getAllEmailFromSheet() = flow {
        val response = smsRetrofit.getAllEmailFromSheet()
        try {
            emit(DataState.Success(response))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    fun getEmailByCheck() = flow {
        val response = smsRetrofit.getEmailByCheck()
        val responseCheck = smsRetrofit.getEmailScanned()
        try {
            emit(DataState.Success(Pair(response, responseCheck)))
        } catch (exception: Exception) {
            emit(DataState.Error(exception))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun sendQrContent(saveObject: SaveObject) = flow<Unit> {
        emit(smsRetrofit.sendQrContent(saveObject))
    }

    suspend fun sendQRScanObject(saveObject: SaveObject) {
        smsRetrofit.sendQrScanned(saveObject)
    }

    suspend fun sendQRScanned(saveObject: SaveObject) = flow {
        emit(smsRetrofit.sendQrScanned(saveObject))
    }

    suspend fun awaitSinglePushValue(data: Pair<String, Bitmap>) = callbackFlow {
        val storageRef = firebaseStorage.reference.root.child("QRCode").child(getRandomString(50))
        val baos = ByteArrayOutputStream()
        data.second.compress(Bitmap.CompressFormat.JPEG, 60, baos)
        val dataByteArray = baos.toByteArray()

        val uploadTask = storageRef.putBytes(dataByteArray)

        val downloadUrlListener = OnSuccessListener<Uri> {
            val saveObject = SaveObject(
                action = "save",
                content = it.toString().trim(),
                from = data.first
            )
            trySend(saveObject)
        }

        val valueListener = OnSuccessListener<UploadTask.TaskSnapshot> {
            storageRef.downloadUrl.addOnSuccessListener(downloadUrlListener)
        }

        val onFailureListener = OnFailureListener { logError("Upload image failure") }

        uploadTask.addOnSuccessListener(valueListener)

        uploadTask.addOnFailureListener(onFailureListener)


        awaitClose {
            uploadTask.removeOnFailureListener(onFailureListener)
            uploadTask.removeOnSuccessListener(valueListener)
        }
    }

    private fun getRandomString(length: Int = 40): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }
}