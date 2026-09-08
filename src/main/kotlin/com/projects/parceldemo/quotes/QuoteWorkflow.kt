package com.projects.parceldemo.quotes

import arrow.core.Either
import arrow.core.raise.either
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

fun create(unvalidatedRequest: UnvalidatedRequest): Either<ValidationError, Quote> = either {
    val validatedRequest = validateRequest(unvalidatedRequest).bind()
    createQuote(validatedRequest).bind()
}

fun createQuote(validatedRequest: ValidatedRequest): Either<ValidationError, Quote> = either {
    val basePrice = validatedRequest.weight.pricingBand().basePrice

    val price = when (validatedRequest.delivery.destination) {
        Destination.DOMESTIC -> basePrice
        Destination.INTERNATIONAL -> basePrice
            .multiply(BigDecimal("1.20"))
            .setScale(2, RoundingMode.HALF_UP)
    }

    val now = LocalDateTime.now()

    Quote(
        validatedRequest = validatedRequest,
        price = price,
        validUntil = now.plusMinutes(30)
    )
}