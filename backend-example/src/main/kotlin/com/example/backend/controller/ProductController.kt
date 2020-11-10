package com.example.backend.controller

import com.example.api.Optional
import com.example.api.Product
import com.example.api.ProductService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController : ProductService {

    override suspend fun get(id: Long): Optional<Product> {
        return when (id) {
            0L -> Optional(null)
            else -> Optional(Product(id, "P$id", 0))
        }
    }

    override suspend fun post(product: Product): Product {
        return product.copy(id = 0)
    }

    @GetMapping("products/search")
    override suspend fun searchByName(name: String?): List<Product> {
        return when (name) {
            null -> listOf(Product(1, "P1", 10), Product(2, "P2", 20))
            "P1" -> listOf(Product(1, "P1", 10))
            else -> emptyList()
        }
    }

    override suspend fun update(
        @RequestBody product: Product
    ): Product {
        return product
    }

    @DeleteMapping("products/{id}")
    override suspend fun delete(
        @PathVariable("id") id: Long
    ): Boolean {
        return when (id) {
            0L -> false
            else -> true
        }
    }

    override suspend fun deleteByPriceGreaterThan(
        @RequestParam("price", required = false, defaultValue = "0") price: Int?
    ): Int {
        return when (price) {
            null -> error("price parameter must not be null.")
            0 -> 2
            else -> 1
        }
    }
}