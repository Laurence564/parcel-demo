package com.projects.parceldemo.quotes

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import java.math.BigDecimal

data class ValidatedRequest(
    val recipientName: RecipientName,
    val weight: Weight,
    val delivery: Delivery
)

@JvmInline
value class RecipientName private constructor(val value: String) {
    companion object {
        fun from(rawName: String): Either<ValidationError, RecipientName> = either {
            ensure(rawName.isNotBlank()) {
                ValidationError.RecipientNameMustNotBeBlank
            }
            ensure(rawName.length <= 50) {
                ValidationError.RecipientNameMustNotExceedFiftyCharacters
            }
            RecipientName(rawName)
        }
    }
}

@JvmInline
value class Weight private constructor(val value: Double) {
    companion object {
        fun from(rawWeight: String): Either<ValidationError, Weight> = either {
            val weight = rawWeight.trim().toBigDecimalOrNull()
                ?: raise(ValidationError.WeightMustBeANumber)

            ensure(weight.scale() <= 2) {
                ValidationError.WeightMustNotExceedTwoDecimalPlaces
            }

            ensure(weight > BigDecimal.ZERO) {
                ValidationError.WeightMustBeGreaterThanZero
            }

            ensure(weight <= BigDecimal("20.00")) {
                ValidationError.WeightMustNotExceedTwentyKg
            }

            Weight(weight.toDouble())
        }
    }
}

fun Weight.pricingBand(): PricingBand =
    when {
        value <= 5.0 -> PricingBand.UP_TO_5_KG
        value <= 10.0 -> PricingBand.UP_TO_10_KG
        value <= 15.0 -> PricingBand.UP_TO_15_KG
        else -> PricingBand.UP_TO_20_KG
    }

@JvmInline
value class CountryCode private constructor(val value: String) {
    companion object {
        fun from(rawCode: String): Either<ValidationError, CountryCode> = either {
            val code = rawCode.trim().uppercase()

            ensure(code.isNotBlank()) {
                ValidationError.DestinationMustNotBeBlank
            }

            ensure(code in Country.entries.map { it.name }) {
                ValidationError.InvalidCountryCode
            }

            CountryCode(code)
        }
    }
}

fun CountryCode.delivery(): Delivery {
    val country = Country.valueOf(value)
    return Delivery(
        country = country,
        destination = if (country == Country.UK) {
            Destination.DOMESTIC
        } else {
            Destination.INTERNATIONAL
        }
    )
}

data class Delivery(
    val country: Country,
    val destination: Destination
)

enum class Destination {
    INTERNATIONAL, DOMESTIC
}

enum class Country {
    UK, US, AU, CA, ES, FR, IT, LT
}
