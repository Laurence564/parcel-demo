package com.projects.parceldemo.quotes

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime

@Configuration
class QuoteConfiguration {

    @Bean
    fun createQuote(): CreateQuoteWorkflow = createQuoteWorkflow(clock = LocalDateTime::now)
}