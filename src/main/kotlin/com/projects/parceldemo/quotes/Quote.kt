package com.projects.parceldemo.quotes

import java.math.BigDecimal
import java.time.LocalDateTime

data class Quote(
    val validatedRequest: ValidatedRequest,
    val price: BigDecimal,
    val validUntil: LocalDateTime
)

enum class PricingBand(val basePrice: BigDecimal) {
    UP_TO_5_KG(BigDecimal("3.00")),
    UP_TO_10_KG(BigDecimal("5.00")),
    UP_TO_15_KG(BigDecimal("7.00")),
    UP_TO_20_KG(BigDecimal("10.00"))
}