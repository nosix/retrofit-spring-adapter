package jp.funmake.spring.web.reactive.retrofit

import org.springframework.core.annotation.AnnotatedElementUtils
import org.springframework.util.StringValueResolver
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.reactive.result.method.RequestMappingInfo
import org.springframework.web.reactive.result.method.RequestMappingInfo.BuilderConfiguration
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.Method
import kotlin.reflect.KClass

class RetrofitRequestMappingHandlerMapping : RequestMappingHandlerMapping() {

    companion object {
        private val MAPPING_ANNOTATIONS: Set<KClass<out Annotation>> = setOf(
            GET::class,
            POST::class,
            PUT::class,
            DELETE::class
        )
    }

    private var embeddedValueResolver: StringValueResolver? = null
    private var config: BuilderConfiguration = BuilderConfiguration()

    // The embeddedValueResolver of super class cannot be referenced, so keep it
    override fun setEmbeddedValueResolver(resolver: StringValueResolver) {
        super.setEmbeddedValueResolver(resolver)
        this.embeddedValueResolver = resolver
    }

    // Generate BuilderConfiguration instance because the instance of super class cannot be referenced
    override fun afterPropertiesSet() {
        config = BuilderConfiguration()
        config.setPatternParser(pathPatternParser)
        config.setContentTypeResolver(contentTypeResolver)
        super.afterPropertiesSet()
    }

    // Override because createRequestMappingInfo is private in super class
    override fun getMappingForMethod(method: Method, handlerType: Class<*>): RequestMappingInfo? {
        val methodInfo = createRequestMappingInfo(method) ?: return null
        val typeInfo = createRequestMappingInfo(handlerType)
        val info = typeInfo?.combine(methodInfo) ?: methodInfo
        for (entry in pathPrefixes.entries) {
            if (entry.value.test(handlerType)) {
                val prefix = embeddedValueResolver?.resolveStringValue(entry.key) ?: entry.key
                return RequestMappingInfo.paths(prefix).options(config).build().combine(info)
            }
        }
        return info
    }

    // Corresponds to Retrofit's GET, POST, PUT, DELETE annotations
    private fun createRequestMappingInfo(element: AnnotatedElement): RequestMappingInfo? {
        val condition = when (element) {
            is Class<*> -> getCustomTypeCondition(element)
            is Method -> getCustomMethodCondition(element)
            else -> error("Invalid element type.")
        }
        val requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping::class.java)
            ?: findInterfaceAnnotation(element)
            ?: return null
        return createRequestMappingInfo(requestMapping, condition)
    }

    // Convert annotations of Retrofit to annotations of Spring
    private fun findInterfaceAnnotation(element: AnnotatedElement): RequestMapping? {
        element.forEachImplementingMethod {
            return when (val annotation = annotations.firstOrNull {
                it.annotationClass in MAPPING_ANNOTATIONS
            }) {
                is GET -> RetrofitRequestMapping(annotation.value, RequestMethod.GET).toRequestMapping()
                is POST -> RetrofitRequestMapping(annotation.value, RequestMethod.POST).toRequestMapping()
                is PUT -> RetrofitRequestMapping(annotation.value, RequestMethod.PUT).toRequestMapping()
                is DELETE -> RetrofitRequestMapping(annotation.value, RequestMethod.DELETE).toRequestMapping()
                else -> null
            }
        }

        return null
    }
}