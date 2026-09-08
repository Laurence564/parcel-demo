| Condition        |               R1 |       R2 |       R3 |       R4 |        R5 |               R6 |
|------------------|-----------------:|---------:|---------:|---------:|----------:|-----------------:|
| Weight = 0       |                T |        F |        F |        F |         F |                F |
| Weight > 0 ≤ 5   |                F |        T |        F |        F |         F |                F |
| Weight > 5 ≤ 10  |                F |        F |        T |        F |         F |                F |
| Weight > 10 ≤ 15 |                F |        F |        F |        T |         F |                F |
| Weight > 15 ≤ 20 |                F |        F |        F |        F |         T |                F |
| Weight > 20      |                F |        F |        F |        F |         F |                T |
| **Action**       | Validation Error | Quote £3 | Quote £5 | Quote £7 | Quote £10 | Validation Error |
