package com.example.backend

import com.example.WebServiceFactory
import com.example.api.Optional
import com.example.api.Product
import com.example.api.ProductService
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductApiTests {

    private val factory = WebServiceFactory("http://localhost:8080/")
    private val productService = factory.create<ProductService>()

    @Test
    fun get(): Unit = runBlocking {
        assertEquals(
            Optional(null),
            productService.get(0)
        )
        assertEquals(
            Optional(Product(1, "P1", 0)),
            productService.get(1)
        )
    }

    @Test
    fun post(): Unit = runBlocking {
        assertEquals(
            Product(0, "P1", 1),
            productService.post(Product(null, "P1", 1))
        )
    }

    @Test
    fun searchByName(): Unit = runBlocking {
        assertEquals(
            listOf(Product(1, "P1", 10), Product(2, "P2", 20)),
            productService.searchByName(null)
        )
        assertEquals(
            listOf(Product(1, "P1", 10)),
            productService.searchByName("P1")
        )
        assertEquals(
            emptyList<Product>(),
            productService.searchByName("P")
        )
    }

    @Test
    fun update(): Unit = runBlocking {
        assertEquals(
            Product(1, "P1", 0),
            productService.update(Product(1, "P1", 0))
        )
    }

    @Test
    fun delete(): Unit = runBlocking {
        assertEquals(
            false,
            productService.delete(0)
        )
        assertEquals(
            true,
            productService.delete(1)
        )
    }

    @Test
    fun deleteByPriceGreaterThan(): Unit = runBlocking {
        assertEquals(
            2,
            productService.deleteByPriceGreaterThan(null)
        )
        assertEquals(
            2,
            productService.deleteByPriceGreaterThan(0)
        )
        assertEquals(
            1,
            productService.deleteByPriceGreaterThan(1)
        )
    }
}