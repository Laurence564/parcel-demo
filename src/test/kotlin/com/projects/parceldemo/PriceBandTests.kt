package com.projects.parceldemo

import com.projects.parceldemo.quotes.UnvalidatedRequest
import com.projects.parceldemo.quotes.ValidationError
import com.projects.parceldemo.quotes.createQuoteWorkflow
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class PriceBandTests {

    private val createQuote = createQuoteWorkflow(clock = LocalDateTime::now)

    @Test
    fun `001 - A quote for a DOMESTIC delivery where a negative weight is selected returns a validation error`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "-0.01",
            country = "UK"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustBeGreaterThanZero

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `002 - A quote for a INTERNATIONAL delivery where a negative weight is selected returns a validation error`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "-0.01",
            country = "FR"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustBeGreaterThanZero

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `003 - A quote for a DOMESTIC delivery where zero weight is selected returns a validation error`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "0.00",
            country = "UK"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustBeGreaterThanZero

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `004 - A quote for a INTERNATIONAL delivery where zero weight is selected returns a validation error`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "0.00",
            country = "US"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustBeGreaterThanZero

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `005 - A quote for a DOMESTIC delivery where 0_01 KG weight is selected quotes 3 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "0.01",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `006 - A quote for a INTERNATIONAL delivery where 0_01 KG weight is selected quotes 3_60 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "0.01",
            country = "AU"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.60")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `007 - A quote for a DOMESTIC delivery where 0_99 KG weight is selected quotes 3 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "0.99",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `008 - A quote for a INTERNATIONAL delivery where 0_99 KG weight is selected quotes 3_60 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "0.99",
            country = "CA"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.60")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `009 - A quote for a DOMESTIC delivery where 4_99 KG weight is selected quotes 3_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "4.99",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `010 - A quote for a INTERNATIONAL delivery where 4_99 KG weight is selected quotes 3_60 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "4.99",
            country = "IT"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.60")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `011 - A quote for a DOMESTIC delivery where 5_00 KG weight is selected quotes 3_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "5.00",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `012 - A quote for a INTERNATIONAL delivery where 5_00 KG weight is selected quotes 3_60 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "5.00",
            country = "ES"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("3.60")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `013 - A quote for a DOMESTIC delivery where 5_01 KG weight is selected quotes 5_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "5.01",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("5.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `014 - A quote for a INTERNATIONAL delivery where 5_01 KG weight is selected quotes 6_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "5.01",
            country = "LT"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("6.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `015 - A quote for a DOMESTIC delivery where 9_99 KG weight is selected quotes 5_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "9.99",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("5.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `016 - A quote for a INTERNATIONAL delivery where 9_99 KG weight is selected quotes 6_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "9.99",
            country = "LT"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("6.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `017 - A quote for a DOMESTIC delivery where 10_0 KG weight is selected quotes 5_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "10.0",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("5.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `018 - A quote for a INTERNATIONAL delivery where 10_0 KG weight is selected quotes 6_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "10.0",
            country = "FR"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("6.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `019 - A quote for a DOMESTIC delivery where 10_01 KG weight is selected quotes 7_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "10.01",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("7.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `020 - A quote for a INTERNATIONAL delivery where 10_01 KG weight is selected quotes 8_40 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "10.01",
            country = "FR"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("8.40")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `021 - A quote for a DOMESTIC delivery where 14_99 KG weight is selected quotes 7_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "14.99",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("7.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `022 - A quote for a INTERNATIONAL delivery where 14_99 KG weight is selected quotes 8_40 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "14.99",
            country = "FR"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("8.40")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `023 - A quote for a DOMESTIC delivery where 15_00 KG weight is selected quotes 7_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "15.00",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("7.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `024 - A quote for a INTERNATIONAL delivery where 15_00 KG weight is selected quotes 8_40 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "15.00",
            country = "FR"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("8.40")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `025 - A quote for a DOMESTIC delivery where 15_01 KG weight is selected quotes 10_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "15.01",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("10.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `026 - A quote for a INTERNATIONAL delivery where 15_01 KG weight is selected quotes 12_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "15.01",
            country = "FR"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("12.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `027 - A quote for a DOMESTIC delivery where 19_99 KG weight is selected quotes 10_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "19.99",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("10.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `028 - A quote for a INTERNATIONAL delivery where 19_99 KG weight is selected quotes 12_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "19.99",
            country = "FR"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("12.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `029 - A quote for a DOMESTIC delivery where 20_00 KG weight is selected quotes 10_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "20.00",
            country = "UK"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("10.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `030 - A quote for a INTERNATIONAL delivery where 20_00 KG weight is selected quotes 12_00 GBP`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "20.00",
            country = "FR"
        )

        // Act
        val actualQuote = createQuote(unvalidatedRequest)
        val expectedQuote = BigDecimal("12.00")

        // Assert
        assertEquals(expectedQuote, actualQuote.getOrNull()?.price)
    }

    @Test
    fun `031 - A quote for a DOMESTIC delivery where 20_01 KG weight is selected returns a validation error`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "20.01",
            country = "UK"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustNotExceedTwentyKg

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

    @Test
    fun `032 - A quote for a INTERNATIONAL delivery where 20_01 KG weight is selected returns a validation error`() {
        // Arrange
        val unvalidatedRequest =  UnvalidatedRequest(
            recipientName = "John Smith",
            weightKg = "20.01",
            country = "FR"
        )

        // Act
        val actualValidationError = createQuote(unvalidatedRequest).leftOrNull()
        val expectedValidationError = ValidationError.WeightMustNotExceedTwentyKg

        // Assert
        assertEquals(expectedValidationError, actualValidationError)
    }

}
