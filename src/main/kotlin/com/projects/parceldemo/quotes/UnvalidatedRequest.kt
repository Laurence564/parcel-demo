package com.projects.parceldemo.quotes

import arrow.core.Either
import arrow.core.raise.either

data class UnvalidatedRequest(
    val recipientName: String,
    val weightKg: String,
    val country: String
)

fun validateRequest(unvalidatedRequest: UnvalidatedRequest): Either<ValidationError, ValidatedRequest> =
    either {
        ValidatedRequest(
            recipientName = RecipientName.from(rawName = unvalidatedRequest.recipientName).bind(),
            weight = Weight.from(rawWeight = unvalidatedRequest.weightKg).bind(),
            delivery = CountryCode.from(rawCode = unvalidatedRequest.country).bind().delivery()
        )
    }