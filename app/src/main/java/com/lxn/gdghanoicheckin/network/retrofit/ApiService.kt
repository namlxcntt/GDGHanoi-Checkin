package com.lxn.gdghanoicheckin.network.retrofit

import com.lxn.gdghanoicheckin.network.model.DataResponse
import com.lxn.gdghanoicheckin.network.model.SaveObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author Nam Lx
 * Create on 18/07/2021
 * Contact me: namlxcntt@gmail.com
 */
interface ApiService {

    /**
     * Method send sms to sheet
     */
    @POST("exec")
    suspend fun sendQrContent(@Body smsObject: SaveObject)

    /**
     * Viết thế này cho nhanh
     * Còn bthg dùng @query
     */
    @GET("exec?method=getAll")
    suspend fun getAllEmailFromSheet() : DataResponse

    @GET("exec?method=check")
    suspend fun getEmailByCheck() : List<String>

}