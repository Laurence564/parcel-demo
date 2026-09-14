## Parcel demo

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
