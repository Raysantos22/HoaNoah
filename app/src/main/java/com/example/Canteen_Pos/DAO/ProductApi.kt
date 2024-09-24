package com.example.possystembw.DAO

import com.example.possystembw.database.CartItem
import com.example.possystembw.database.Product
import com.example.possystembw.database.TransactionRecord
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @POST("upload_database")
    suspend fun uploadDatabase(@Body products: List<Product>): Response<ApiResponse>

    @GET("cart_items")
    suspend fun getAllCartItems(): List<CartItem>

    @POST("/upload_products")
    suspend fun uploadProducts(@Body products: List<Product>): Response<ApiResponse>

    @POST("/upload_cart_items")
    suspend fun uploadCartItems(@Body cartItems: List<CartItem>): Response<ApiResponse>

    @POST("/upload_transactions")
    suspend fun uploadTransactions(@Body transactions: List<TransactionRecord>): Response<ApiResponse>

    @GET("api/products")
    suspend fun getProducts(): Response<List<Product>>

    @GET("api/cart_items")
    suspend fun getCartItems(): Response<List<CartItem>>

    @GET("api/transactions")
    suspend fun getTransactions(): Response<List<TransactionRecord>>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Response<ApiResponse>

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Response<ApiResponse>

    @DELETE("products")
    suspend fun deleteAllProducts(): Response<ApiResponse>
}

data class ApiResponse(
    val message: String,
    val data: Any?
)