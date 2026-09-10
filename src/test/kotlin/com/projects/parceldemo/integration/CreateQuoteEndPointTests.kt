package com.projects.parceldemo.integration

import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateQuoteEndPointTests {

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
    }

    private fun requestBody(weightKg: String, country: String) = """
        {
          "recipientName": "John Smith",
          "weightKg": "$weightKg",
          "country": "$country"
        }
    """.trimIndent()

    @ParameterizedTest(name = "{0} KG to {1} quotes {2} GBP")
    @CsvSource(
        "5.00,  UK, 3.00,  DOMESTIC",
        "5.01,  UK, 5.00,  DOMESTIC",
        "10.01, UK, 7.00,  DOMESTIC",
        "15.01, UK, 10.00, DOMESTIC",
        "5.00,  FR, 3.60,  INTERNATIONAL",
        "15.01, FR, 12.00, INTERNATIONAL"
    )
    fun `a valid request returns a quote at the expected price`(
        weightKg: String,
        country: String,
        expectedPrice: String,
        expectedDestination: String
    ) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody(weightKg, country))
            .post("/quotes/new")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("price", equalTo(expectedPrice))
            .body("destination", equalTo(expectedDestination))
            .body("country", equalTo(country))
            .body("recipientName", equalTo("John Smith"))
    }

    @ParameterizedTest(name = "{0} / {1} is rejected with {2}")
    @CsvSource(
        "20.01, UK, Maximum weight value exceeds the 20 KG threshold.",
        "0.00,  UK, Please enter a weight greater than zero.",
        "abc,   UK, Please enter a number in the weight field.",
        "5.00,  XX, Please enter a valid country code."
    )
    fun `an invalid request returns a 400 with the validation message`(
        weightKg: String,
        country: String,
        expectedMessage: String
    ) {
        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(requestBody(weightKg, country))
            .post("/quotes/new")
            .then()
            .statusCode(400)
            .body("errors[0].message", equalTo(expectedMessage))
    }
}