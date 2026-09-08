package com.projects.parceldemo.global

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@RestControllerAdvice
class ValidationHandler {

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<Map<String, Any>> {
        val error = mapOf(
            "code" to "${e.statusCode}",
            "message" to (e.reason)
        )
       return ResponseEntity
           .status(e.statusCode)
           .body(mapOf("errors" to listOf(error)))
    }
}