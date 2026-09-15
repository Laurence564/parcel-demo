## Parcel demo

![Tests](https://github.com/laurence564/parcel-demo/actions/workflows/test.yml/badge.svg)
![Kotlin](https://img.shields.io/badge/kotlin-2.3.21-blue?logo=kotlin)
![Spring Boot](https://img.shields.io/badge/spring--boot-4.1.1-green?logo=springboot)

Parcel demo is a basic quote generator for delivering parcels domestically and internationally.
It highlights the power of Functional Programming and DDD to build highly testable code iteratively on small units of code.

[TEST APPROACH](#test-approach) | [UNIT TESTING](#unit-testing) | [INTEGRATION TESTING](#integration-testing) | [SYSTEM TESTING](#system-testing) | [ARCHITECTURE CHOICES](#architecture-choices) 
##


### Test approach

Even simple ideas get complex quickly transferring into code, therefore testing tables are a great way to zoom out and
get an overarching view point of that complexity.
In an application focussing on pricing bands, the equivalence partitioning table and boundary analysis are great
options.

#### Equivalence Partition (base costs)

| Equivalence Partition | Weight range (KG) | Valid/Invalid | Expected result  | Core test input |
|-----------------------|-------------------|---------------|------------------|-----------------|
| EP1                   | less than 0.01    | Invalid       | Validation Error | `0`             |
| EP2                   | 0.01 to 5         | Valid         | Base Quote £3    | `1`, `5`        |
| EP3                   | 5.01 to 10        | Valid         | Base Quote £5    | `6`, `10`       |
| EP4                   | 10.01 to 15       | Valid         | Base Quote £7    | `11`, `15`      |
| EP5                   | 15.01 to 20       | Valid         | Base Quote £10   | `16`, `20`      |
| EP6                   | greater than 20   | Invalid       | Validation Error | `21`            |

<br>
  
#### Boundary Analysis (base costs)

| Boundary Analysis | Boundary (KG) | Below (KG) | On (KG) | Above (KG) | Expected Oucome (Below/On/Above) |
|-------------------|---------------|------------|---------|------------|----------------------------------|
| BA1               | 0             | -0.01      | 0       | 0.01       | Error / Error / £3               |
| BA2               | 5             | 4.99       | 5       | 5.01       | £3 / £3 / £5                     |
| BA3               | 10            | 9.99       | 10      | 10.01      | £5 / £5 / £7                     |
| BA4               | 15            | 14.99      | 15      | 15.01      | £7 / £7 / £10                    |
| BA5               | 20            | 19.99      | 20      | 20.01      | £10 / £10 / Error                |

<br>


#### Decision Table

|                                    |   R1 |   R2 |
|------------------------------------|-----:|-----:|
| C1. Destination is Domestic        |    T |    F |
| C2. Destination is International   |    F |    T |
| ---------------------------------- | ---- | ---- |
| Base rate applied                  |    X |    X |
| International shipping added (20%) |      |    X |

#
### Unit Testing
In the testing pyramid, this is where the bulk tests live. The unit under test is a pure partial function where 
the clock dependency can be easily passed in as a parameter.

```kotlin
private val createQuote = createQuoteWorkflow(clock = LocalDateTime::now)
```

Using the Arrow library, the workflow function can return either a Quote or a ValidationError which is enforced 
by the interface signature.

```kotlin
fun interface CreateQuoteWorkflow {
    operator fun invoke(unvalidatedRequest: UnvalidatedRequest): Either<ValidationError, Quote>
}
```

Because of this separation of concerns, tests are concise and are easy to reason about. 
Quick links to unit tests:
  * [FieldValidationTests.kt](src/test/kotlin/com/projects/parceldemo/unit/FieldValidationTests.kt)
  * [PriceBandTests.kt](src/test/kotlin/com/projects/parceldemo/unit/PriceBandTests.kt)

#
### Integration Testing
Moving up the pyramid, the tests get fewer and the scope gets wider. The unit tests prove the pricing rules in
isolation; these tests prove the route through [QuoteController.kt](src/main/kotlin/com/projects/parceldemo/quotes/QuoteController.kt). The application is booted for real and driven over HTTP.

Coverage here is intentionally thin. We are looking for high yielding tests to:
* Test a handful of valid domestic and international quote requests
* Test the API handles all the failure cases and the respective error messages [ValidationError.kt](src/main/kotlin/com/projects/parceldemo/quotes/ValidationError.kt)

Quick links to integration tests:
* [CreateQuoteEndPointTests.kt](src/test/kotlin/com/projects/parceldemo/integration/CreateQuoteEndPointTests.kt)

#
### System Testing
Now at the top of the pyramid, we are testing the application as a user would in order to get value from the product.
We've taken a couple of tests validate the happy path for both a domestic and international delivery. The majority of
the effort goes into checking the negative tests to make sure the user is given all the information they need to correct
any invalid values they enter when requesting a quote.

By default, tests are run as headless. This is mainly so that they can run within GitHub Actions without the need of 
adding any flags in the GitHub Actions workflow.

```kotlin
private val headless: Boolean = System.getenv("E2E_HEADED") == null
```

Given the tests re-use the same elements on page, we have created a POM for the form. Ids are the favoured selector
due to the fact they don't need to change unlike other elements which often get updated as development moves on.

```kotlin
    private fun requestQuote(unvalidatedRequest: UnvalidatedRequest): Page {
        val page = browser.newContext().newPage()

        page.navigate(baseUrl)
        page.getByTestId("recipient-name").fill(unvalidatedRequest.recipientName)
        page.getByTestId("weight-kg").fill(unvalidatedRequest.weightKg)
        page.getByTestId("country").selectOption(unvalidatedRequest.country)
        page.getByTestId("get-quote").click()

        return page
    }
```

As before, system tests can remain small and easy to reason about. Again Ids are used to get the error text because we
want to keep our tests robust enough to survive front-end code changes. In bigger teams, communication is key when changing
selectors (especially Ids) because tests can be fixed without false negatives coming from the CI/CD or nightly test runs.

```kotlin
    @Test
    fun `an international quote applies the surcharge`() {
        val page = requestQuote(
            UnvalidatedRequest(recipientName = "John Smith", weightKg = "5.01", country = "FR")
        )

        assertThat(page.getByTestId("price")).hasText("£6.00")
        assertThat(page.getByTestId("destination")).hasText("INTERNATIONAL")
    }
```

Quick links:
  * [PositiveQuoteJourneyTests.kt](src/test/kotlin/com/projects/parceldemo/e2e/PositiveQuoteJourneyTests.kt)
  * [NegativeQuoteJourneyTests.kt](src/test/kotlin/com/projects/parceldemo/e2e/NegativeQuoteJourneyTests.kt) 


#
### Architecture Choices
I came across functional programming and domain driven design after getting frustrated that no matter how hard I tried
to keep my code simple, complexity always caught up. Branching on boolean after boolean made code difficult to follow
when revisiting it even a few weeks later, and knowing how a change would ripple through a function became a task in
itself. My testing experience usually caught the side effects, but relying on that wasn't acceptable to me. I'd
practiced OOP, broken code into classes and followed SOLID, but I wanted something stricter.

This is where I came across Scott Wlaschin's *Domain Modeling Made Functional*. One of its key concepts is defining
errors out of existence by making unrepresentable states impossible.

``` kotlin
@JvmInline
value class Weight private constructor(val value: Double) {
    companion object {
        fun from(rawWeight: String): Either<ValidationError, Weight> = either { ... }
    }
}
```

The private constructor is mechanism which allows us to be strict about enforcing the expected state.
Nothing outside the class can write `Weight(-5.0)`, and `from` is the only way in — it returns an `Either`,
so the caller has to handle the failure before they ever hold a `Weight`. A negative
weight isn't a value that gets rejected; it's a value that cannot be expressed.

The payoff shows up one function later:

``` kotlin
fun Weight.pricingBand(): PricingBand =
    when {
        value <= 5.0 -> PricingBand.UP_TO_5_KG
        ...
    }
```

Note the return type — `PricingBand`. Defensive code is not needed here because this has already been done by
what is known as "validation at the boundary". This means that the domain code we are working with becomes much
simpler to work with, and we can focus on purely the pricing functionality.
Furthermore, validation happens in one place and the Arrow library makes this read like a check list:

``` kotlin
  ensure(weight.scale() <= 2) {
    ValidationError.WeightMustNotExceedTwoDecimalPlaces
  }

  ensure(weight > BigDecimal.ZERO) {
    ValidationError.WeightMustBeGreaterThanZero
  }
```
