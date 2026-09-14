## Parcel demo

___
Parcel demo is a basic quote generator for delivering parcels domestically and internationally.
It highlights the power of functional programming and DDD to cover the tests levels:

* Unit tests
* Integration tests
* System testing

### High level test analysis

Even simple ideas get complex quickly transferring into code, therefore testing tables are a great way to zoom out and
get an overarching view point of that complexity.
In an application focussing on pricing bands, the equivalence partitioning table and boundary analysis are great
options.

___

#### Equivalence Partition (base costs)

| Equivalence Partition | Weight range (KG) | Valid/Invalid | Expected result  | Core test input |
|-----------------------|-------------------|---------------|------------------|-----------------|
| EP1                   | less than 0.01    | Invalid       | Validation Error | `0`             |
| EP2                   | 0.01 to 5         | Valid         | Base Quote £3    | `1`, `5`        |
| EP3                   | 5.01 to 10        | Valid         | Base Quote £5    | `6`, `10`       |
| EP4                   | 10.01 to 15       | Valid         | Base Quote £7    | `11`, `15`      |
| EP5                   | 15.01 to 20       | Valid         | Base Quote £10   | `16`, `20`      |
| EP6                   | greater than 20   | Invalid       | Validation Error | `21`            |

___

#### Boundary Analysis (base costs)

| Boundary Analysis | Boundary (KG) | Below (KG) | On (KG) | Above (KG) | Expected Oucome (Below/On/Above) |
|-------------------|---------------|------------|---------|------------|----------------------------------|
| BA1               | 0             | -0.01      | 0       | 0.01       | Error / Error / £3               |
| BA2               | 5             | 4.99       | 5       | 5.01       | £3 / £3 / £5                     |
| BA3               | 10            | 9.99       | 10      | 10.01      | £5 / £5 / £7                     |
| BA4               | 15            | 14.99      | 15      | 15.01      | £7 / £7 / £10                    |
| BA5               | 20            | 19.99      | 20      | 20.01      | £10 / £10 / Error                |
