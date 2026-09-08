Key:
BA = Boundary Analysis
EP = Equivalence Partitioning
TC = Test Case

| Test   |  Weight | Destination   | Expected result  | Derived from |
|--------|--------:|---------------|------------------|--------------|
| TC 001 | `-0.01` | Domestic      | Validation Error | EP           |
| TC 002 | `-0.01` | International | Validation Error | EP           |
| TC 003 |     `0` | Domestic      | Validation Error | EP           |
| TC 004 |     `0` | International | Validation Error | EP           |
| TC 005 |  `0.01` | Domestic      | Quote £3         | EP           |
| TC 006 |  `0.01` | International | Quote £3.60      | EP           |
| TC 007 |  `0.99` | Domestic      | Quote £3         | EP           |
| TC 008 |  `0.99` | International | Quote £3.60      | EP           |
| TC 009 |  `4.99` | Domestic      | Quote £3         | EP           |
| TC 010 |  `4.99` | International | Quote £3.60      | EP           |
| TC 011 |     `5` | Domestic      | Quote £3         | BA           |
| TC 012 |     `5` | International | Quote £3.60      | BA           |
| TC 013 |  `5.01` | Domestic      | Quote £5         | EP           |
| TC 014 |  `5.01` | International | Quote £6         | EP           |
| TC 015 |  `9.99` | Domestic      | Quote £5         | EP           |
| TC 016 |  `9.99` | International | Quote £6         | EP           |
| TC 017 |    `10` | Domestic      | Quote £5         | BA           |
| TC 018 |    `10` | International | Quote £6         | BA           |
| TC 019 | `10.01` | Domestic      | Quote £7         | EP           |
| TC 020 | `10.01` | International | Quote £8.40      | EP           |
| TC 021 | `14.99` | Domestic      | Quote £7         | EP           |
| TC 022 | `14.99` | International | Quote £8.40      | EP           |
| TC 023 |    `15` | Domestic      | Quote £7         | BA           |
| TC 024 |    `15` | International | Quote £8.40      | BA           |
| TC 025 | `15.01` | Domestic      | Quote £10        | EP           |
| TC 026 | `15.01` | International | Quote £12        | EP           |
| TC 027 | `19.99` | Domestic      | Quote £10        | EP           |
| TC 028 | `19.99` | International | Quote £12        | EP           |
| TC 029 |    `20` | Domestic      | Quote £10        | BA           |
| TC 030 |    `20` | International | Quote £12        | BA           |
| TC 031 | `20.01` | Domestic      | Validation Error | EP           |
| TC 032 | `20.01` | International | Validation Error | EP           |
