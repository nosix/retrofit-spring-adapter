English | [日本語(Japanese)](README_ja.md)

# retrofit-spring-adapter

This project provides an adapter to convert Retrofit annotations to Spring annotations.

When you extend the WebFluxRegistrations class and apply the adapter,
the adapter converts the annotations automatically.

The conversion rules are as follows:

| Retrofit  | Spring           |
| --------- | ---------------- |
| `@GET`    | `@GetMapping`    |
| `@POST`   | `@PostMapping`   |
| `@PUT`    | `@PutMapping`    |
| `@DELETE` | `@DeleteMapping` |
| `@Query`  | `@RequestParam`  |
| `@Path`   | `@PathVariable`  |
| `@Body`   | `@RequestBody`   |

## Usage

```kotlin
// build.gradle.kts

repositories {
    /* ... */
    maven { url = uri("https://raw.githubusercontent.com/nosix/retrofit-spring-adapter/main/release") }
}

dependencies {
    /* ... */ 
    implementation("jp.funmake:retrofit-spring-adapter:$version")
}
```

```kotlin
// RetrofitConfig.kt

@Configuration
class RetrofitConfig : WebFluxRegistrations {
    override fun getRequestMappingHandlerMapping() = RetrofitRequestMappingHandlerMapping()
    override fun getRequestMappingHandlerAdapter() = RetrofitRequestMappingHandlerAdapter()
}
```

```kotlin
// ProductService.kt

// Define a Retrofit annotated interface
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
```

```kotlin
// ProductController.kt

@RestController
class ProductController : ProductService { // Implement the Retrofit annotated interface

    // @GetMapping is generated from @GET
    // @PathVariable is generated from @Path
    override suspend fun get(id: Long): Optional<Product> {
        TODO("implement")
    }

    // @PostMapping is generated from @POST
    // @RequestBody is generated from @Body
    override suspend fun post(product: Product): Product {
        TODO("implement")
    }

    // When define @GetMapping, generation is disabled
    // @RequestParam is generated from @Query
    @GetMapping("products/search")
    override suspend fun searchByName(name: String?): List<Product> {
        TODO("implement")
    }

    // @PutMapping is generated from @PUT
    // When define @RequestBody, generation is disabled
    override suspend fun update(@RequestBody product: Product): Product {
        TODO("implement")
    }

    // When define @DeleteMapping, generation is disabled
    // When define @PathVariable, generation is disabled
    @DeleteMapping("products/{id}")
    override suspend fun delete(@PathVariable("id") id: Long): Boolean {
        TODO("implement")
    }

    // @DeleteMapping is generated from @DELETE
    // When define @RequestParam, generation is disabled
    override suspend fun deleteByPriceGreaterThan(
        @RequestParam("price", required = false, defaultValue = "0") price: Int?
    ): Int {
        TODO("implement")
    }
}
```

See [api-example](api-example) and [backend-example](backend-example) for specific examples.

## Version

The version string has the following rules:
 
```
{Spring Boot version}.{Retrofit version}.{patch version}
```

For example、Spring Boot version is 2.4.0, Retrofit version is 2.9.0, then version string is `0204.0209.0`.
