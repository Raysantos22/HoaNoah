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
    suspend fun uploadDatabase(@Body products: List<Product>): Response<Unit>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Product

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product

    @DELETE("products")
    suspend fun deleteAllProducts()

    @GET("cart_items")
    suspend fun getAllCartItems(): List<CartDao>

    @POST("/upload_products")
    suspend fun uploadProducts(@Body products: List<Product>): Response<String>

    @POST("/upload_cart_items")
    suspend fun uploadCartItems(@Body cartItems: List<CartItem>): Response<String>

    @POST("/upload_transactions")
    suspend fun uploadTransactions(@Body transactions: List<TransactionRecord>): Response<String>


}
