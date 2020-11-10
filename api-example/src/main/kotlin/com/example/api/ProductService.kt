package com.example.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("products/{id}")
    suspend fun get(@Path("id") id: Long): Optional<Product>

    @POST("products")
    suspend fun post(@Body product: Product): Product

    @GET("products/search")
    suspend fun searchByName(@Query("name") name: String? = null): List<Product>

    @PUT("products/{id}")
    suspend fun update(@Body product: Product): Product

    @DELETE("products/{id}")
    suspend fun delete(@Path("id") id: Long): Boolean

    @DELETE("products")
    suspend fun deleteByPriceGreaterThan(@Query("price") price: Int? = null): Int
}