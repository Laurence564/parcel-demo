package com.projects.parceldemo.e2e

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import com.projects.parceldemo.quotes.UnvalidatedRequest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NegativeQuoteJourneyTests {

    @LocalServerPort
    private var port: Int = 0

    private val baseUrl: String get() = "http://localhost:$port"

    companion object {
        private val headless: Boolean = System.getenv("E2E_HEADED") == null

        private lateinit var playwright: Playwright
        private lateinit var browser: Browser

        @JvmStatic
        @BeforeAll
        fun launchBrowser() {
            playwright = Playwright.create()
            browser = playwright.chromium().launch(com.microsoft.playwright
                .BrowserType
                .LaunchOptions()
                .setHeadless(headless)
            )
        }

        @JvmStatic
        @AfterAll
        fun closeBrowser() {
            browser.close()
            playwright.close()
        }
    }

    @Test
    fun `Request for a quote is submitted with both the recipient name and weight left blank`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "", weightKg = "", country = "UK")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Please provide a recipient name.")
    }

    @Test
    fun `Request for a quote is submitted with a blank recipient name`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "", weightKg = "10.00", country = "US")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Please provide a recipient name.")
    }

    @Test
    fun `Request for a quote is submitted with a recipient name exceeding 50 characters`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "a".repeat(51), weightKg = "10.00", country = "UK")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Recipient name must not exceed fifty characters.")
    }

    @Test
    fun `Request for a quote is submitted with a blank weight`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "John Smith", weightKg = "", country = "AU")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Please enter a number in the weight field.")
    }

    @Test
    fun `Request for a quote is submitted with a non-numeric weight`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "John Smith", weightKg = "fivekilograms", country = "UK")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Please enter a number in the weight field.")
    }

    @Test
    fun `Request for a quote is submitted with a weight of zero`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "John Smith", weightKg = "0.00", country = "ES")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Please enter a weight greater than zero.")
    }

    @Test
    fun `Request for a quote is submitted with a negative weight`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "John Smith", weightKg = "-1.00", country = "UK")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Please enter a weight greater than zero.")
    }

    @Test
    fun `Request for a quote exceeds the allowed weight limit`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "John Smith", weightKg = "20.01", country = "FR")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Maximum weight value exceeds the 20 KG threshold.")
    }

    @Test
    fun `Request for a quote exceeds the two decimal places`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "John Smith", weightKg = "20.111", country = "UK")
        )

        assertThat(page.getByTestId("error"))
            .hasText("Please enter a weight with a maximum of two decimal places.")
    }

    private fun requestQuote(unvalidatedRequest: UnvalidatedRequest): Page {
        val page = browser.newContext().newPage()

        page.navigate(baseUrl)
        page.getByTestId("recipient-name").fill(unvalidatedRequest.recipientName)
        page.getByTestId("weight-kg").fill(unvalidatedRequest.weightKg)
        page.getByTestId("country").selectOption(unvalidatedRequest.country)
        page.getByTestId("get-quote").click()

        return page
    }
}