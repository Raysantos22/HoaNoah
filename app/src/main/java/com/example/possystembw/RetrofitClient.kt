package com.example.possystembw

import android.util.Log
import com.example.possystembw.DAO.ProductApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    fun checkConnection(): retrofit2.Call<Unit>
}

object RetrofitClient {
    private const val BASE_URL = "http://localhost:3000/"  // Replace with your server's IP address
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

    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun isConnected(): Boolean {
        return try {
            val response = apiService.checkConnection().execute()
            response.isSuccessful.also { isSuccessful ->
                if (isSuccessful) {
                    Log.i(TAG, "Successfully connected to $BASE_URL")
                } else {
                    Log.w(TAG, "Failed to connect to $BASE_URL. Code: ${response.code()}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error checking connection: ${e.message}")
            false
        }
    }
}