package com.projects.parceldemo.quotes

sealed interface ValidationError {
    data object RecipientNameMustNotBeBlank : ValidationError
    data object RecipientNameMustNotExceedFiftyCharacters : ValidationError

    data object WeightMustBeANumber : ValidationError
    data object WeightMustBeGreaterThanZero : ValidationError
    data object WeightMustNotExceedTwentyKg : ValidationError

    data object DestinationMustNotBeBlank : ValidationError
    data object InvalidCountryCode : ValidationError
}
