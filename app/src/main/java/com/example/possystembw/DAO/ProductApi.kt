package com.example.possystembw.DAO

import com.example.possystembw.database.Product
import retrofit2.http.*

interface ProductApi {
    @GET("products")
    suspend fun getAllProducts(): List<Product>

    @POST("products")
    suspend fun createProduct(@Body product: Product): Product

    @PUT("products/{id}")
    suspend fun updateProduct(@Path("id") id: Int, @Body product: Product): Product

    @DELETE("products")
    suspend fun deleteAllProducts()
}