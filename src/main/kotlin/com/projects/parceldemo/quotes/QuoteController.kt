package com.projects.parceldemo.quotes

import arrow.core.getOrElse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/quotes")
class QuoteController(private val createQuote: CreateQuoteWorkflow) {

    @PostMapping("/new")
    fun acceptWorkoutInvite(@RequestBody unvalidatedRequest: UnvalidatedRequest): QuoteResponse {

        val quote = createQuote(unvalidatedRequest).getOrElse { e: ValidationError ->
            throw e.toResponseStatusException()
        }

        return quote.mapToResponse()
    }
}
