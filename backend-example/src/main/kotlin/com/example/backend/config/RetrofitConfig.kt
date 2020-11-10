package com.example.backend.config

import jp.funmake.spring.web.reactive.retrofit.RetrofitRequestMappingHandlerAdapter
import jp.funmake.spring.web.reactive.retrofit.RetrofitRequestMappingHandlerMapping
import org.springframework.boot.autoconfigure.web.reactive.WebFluxRegistrations
import org.springframework.context.annotation.Configuration

@Configuration
class RetrofitConfig : WebFluxRegistrations {
    override fun getRequestMappingHandlerMapping() = RetrofitRequestMappingHandlerMapping()
    override fun getRequestMappingHandlerAdapter() = RetrofitRequestMappingHandlerAdapter()
}