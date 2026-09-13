package com.projects.parceldemo.e2e

import com.microsoft.playwright.Browser
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PositiveQuoteJourneyTests {

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
            browser = playwright.chromium().launch(
                com.microsoft.playwright.BrowserType.LaunchOptions().setHeadless(headless)
            )
        }

        @JvmStatic
        @AfterAll
        fun closeBrowser() {
            browser.close()
            playwright.close()
        }
    }

    private fun requestQuote(recipientName: String, weightKg: String, country: String): Page {
        val page = browser.newContext().newPage()

        page.navigate(baseUrl)
        page.getByTestId("recipient-name").fill(recipientName)
        page.getByTestId("weight-kg").fill(weightKg)
        page.getByTestId("country").selectOption(country)
        page.getByTestId("get-quote").click()

        return page
    }

    @Test
    fun `a domestic quote is displayed for a valid request`() {
        val page = requestQuote(recipientName = "John Snow", weightKg = "5.01", country = "UK")

        assertThat(page.getByTestId("price")).hasText("£5.00")
        assertThat(page.getByTestId("destination")).hasText("DOMESTIC")
    }

    @Test
    fun `an international quote applies the surcharge`() {
        val page = requestQuote(recipientName = "John Smith", weightKg = "5.01", country = "FR")

        assertThat(page.getByTestId("price")).hasText("£6.00")
        assertThat(page.getByTestId("destination")).hasText("INTERNATIONAL")
    }

}
