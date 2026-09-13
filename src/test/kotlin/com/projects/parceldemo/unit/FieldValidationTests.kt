package com.projects.parceldemo.unit

import com.projects.parceldemo.quotes.UnvalidatedRequest
import com.projects.parceldemo.quotes.ValidationError
import com.projects.parceldemo.quotes.createQuoteWorkflow
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import kotlin.test.assertEquals

class FieldValidationTests {

    private val createQuote = createQuoteWorkflow(clock = LocalDateTime::now)

    @Test
    fun `The recipient name is blank`() {
         // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "",
            weightKg = "10",
            country = "UK"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.RecipientNameMustNotBeBlank

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `The recipient name exceeds 50 characters`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "a".repeat(51),
            weightKg = "10",
            country = "UK"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.RecipientNameMustNotExceedFiftyCharacters

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `The weight is blank`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "Jon Snow",
            weightKg = "",
            country = "UK"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustBeANumber

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `The weight is not a number`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "Jon Snow",
            weightKg = "fivekilograms",
            country = "UK"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustBeANumber

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }
}