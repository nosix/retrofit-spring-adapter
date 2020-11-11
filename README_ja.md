[English](README.md) | 日本語(Japanese)

# retrofit-spring-adapter

RetrofitのアノテーションをSpringのアノテーションに変換するアダプタを提供します。

WebFluxRegistrationsクラスを拡張して適用すると、自動的にアノテーションが変換されます。

変換規則は以下の通りです。

| Retrofit  | Spring           |
| --------- | ---------------- |
| `@GET`    | `@GetMapping`    |
| `@POST`   | `@PostMapping`   |
| `@PUT`    | `@PutMapping`    |
| `@DELETE` | `@DeleteMapping` |
| `@Query`  | `@RequestParam`  |
| `@Path`   | `@PathVariable`  |
| `@Body`   | `@RequestBody`   |

## 使用方法

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

// Retrofitのアノテーションを付与したインターフェイスを定義します
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
class ProductController : ProductService { // Retrofitのアノテーションを付与したインターフェイスを実装します

    // @GETから@GetMappingが生成されます
    // @Pathから@PathVariableが生成されます
    override suspend fun get(id: Long): Optional<Product> {
        TODO("implement")
    }

    // @POSTから@PostMappingが生成されます
    // @Bodyから@RequestBodyが生成されます
    override suspend fun post(product: Product): Product {
        TODO("implement")
    }

    // @GetMappingを定義すると、生成は無効化されます
    // @Queryから@RequestParamが生成されます
    @GetMapping("products/search")
    override suspend fun searchByName(name: String?): List<Product> {
        TODO("implement")
    }

    // @PUTから@PutMappingが生成されます
    // @RequestBodyを定義すると、生成は無効化されます
    override suspend fun update(@RequestBody product: Product): Product {
        TODO("implement")
    }

    // @DeleteMappingを定義すると、生成は無効化されます
    // @PathVariableを定義すると、生成は無効化されます
    @DeleteMapping("products/{id}")
    override suspend fun delete(@PathVariable("id") id: Long): Boolean {
        TODO("implement")
    }

    // @DELETEから@DeleteMappingが生成されます
    // @RequestParamを定義すると、生成は無効化されます
    override suspend fun deleteByPriceGreaterThan(
        @RequestParam("price", required = false, defaultValue = "0") price: Int?
    ): Int {
        TODO("implement")
    }
}
```

具体例は [api-example](api-example) と [backend-example](backend-example) を参照してください。

## バージョン

バージョン文字列は以下の規則になっています。
 
```
{Spring Boot のバージョン}.{Retrofit のバージョン}.{パッチバージョン}
```

例えば、Spring Boot バージョン 2.4.0、Retrofit バージョン 2.9.0 の場合は `0204.0209.0` です。
