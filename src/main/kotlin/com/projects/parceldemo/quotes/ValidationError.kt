package com.projects.parceldemo.quotes

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

sealed interface ValidationError {
    data object RecipientNameMustNotBeBlank : ValidationError
    data object RecipientNameMustNotExceedFiftyCharacters : ValidationError

    data object WeightMustBeANumber : ValidationError
    data object WeightMustBeGreaterThanZero : ValidationError
    data object WeightMustNotExceedTwentyKg : ValidationError

    data object DestinationMustNotBeBlank : ValidationError
    data object InvalidCountryCode : ValidationError
}

fun ValidationError.toResponseStatusException(): ResponseStatusException {
    return when (this) {
        ValidationError.DestinationMustNotBeBlank ->
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter a destination.")

        ValidationError.InvalidCountryCode ->
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter a valid country code.")

        ValidationError.RecipientNameMustNotBeBlank ->
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Please provide a recipient name.")

        ValidationError.RecipientNameMustNotExceedFiftyCharacters ->
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipient name must not exceed fifty characters.")

        ValidationError.WeightMustBeANumber ->
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter a number in the weight field.")

        ValidationError.WeightMustBeGreaterThanZero ->
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Please enter a weight greater than zero.")

        ValidationError.WeightMustNotExceedTwentyKg ->
            ResponseStatusException(HttpStatus.BAD_REQUEST, "Maximum weight value exceeds the 20 KG threshold.")
    }
}