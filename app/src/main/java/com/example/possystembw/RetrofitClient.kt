package com.example.possystembw

import android.util.Log
import com.example.possystembw.DAO.ProductApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://10.0.2.2:3000/"  // Replace with your server's IP address if different
    private const val TAG = "RetrofitClient"

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(TAG, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: ProductApi by lazy {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            Log.i(TAG, "Retrofit instance created successfully")
            retrofit.create(ProductApi::class.java)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating Retrofit instance: ${e.message}")
            throw e
        }
    }
}