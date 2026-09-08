package com.projects.parceldemo.quotes

import arrow.core.Either
import arrow.core.raise.either
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime


fun interface CreateQuoteWorkflow {
    operator fun invoke(unvalidatedRequest: UnvalidatedRequest): Either<ValidationError, Quote>
}

fun createQuoteWorkflow(clock: () -> LocalDateTime): CreateQuoteWorkflow {
    return CreateQuoteWorkflow { unvalidatedRequest ->
        createQuote(clock(), unvalidatedRequest)
    }
}

fun createQuote(clock: LocalDateTime, unvalidatedRequest: UnvalidatedRequest): Either<ValidationError, Quote> = either {
    val validatedRequest = validateRequest(unvalidatedRequest).bind()
    val basePrice = validatedRequest.weight.pricingBand().basePrice

    val price = when (validatedRequest.delivery.destination) {
        Destination.DOMESTIC -> basePrice
        Destination.INTERNATIONAL -> basePrice
            .multiply(BigDecimal("1.20"))
            .setScale(2, RoundingMode.HALF_UP)
    }

    Quote(
        validatedRequest = validatedRequest,
        price = price,
        validUntil = clock.plusMinutes(30)
    )
}
