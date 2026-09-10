package com.projects.parceldemo.quotes

data class QuoteResponse(
    val recipientName: String,
    val weightKg: Double,
    val country: String,
    val destination: String,
    val price: String,
    val validUntil: String
)
