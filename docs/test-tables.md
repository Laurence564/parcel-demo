### Equivalence Partitioning

| Equivalence Partition | Weight range (KG) | Valid/Invalid | Expected result  | Core test condition |
|-----------------------|-------------------|---------------|------------------|---------------------|
| EP1                   | less than 0.01    | Invalid       | Validation Error | `0`                 |
| EP2                   | 0.01 to 5         | Valid         | Base Quote £3    | `1`, `5`            |
| EP3                   | 5.01 to 10        | Valid         | Base Quote £5    | `6`, `10`           |
| EP4                   | 10.01 to 15       | Valid         | Base Quote £7    | `11`, `15`          |
| EP5                   | 15.01 to 20       | Valid         | Base Quote £10   | `16`, `20`          |
| EP6                   | greater than 20   | Invalid       | Validation Error | `21`                |

### Boundary Analysis

| Boundary Analysis | Weight range (KG) | Valid/Invalid | Expected result  | Core test condition |
|-------------------|-------------------|---------------|------------------|---------------------|
| BA1               | less than 0.01    | Invalid       | Validation Error | `-0.01`, `0`        |
| BA2               | 0.01 to 5         | Valid         | Base Quote £3    | `0.01`, `4.99`      |
| BA3               | 5.01 to 10        | Valid         | Base Quote £5    | `5.01`, `9.99`      |
| BA4               | 10.01 to 15       | Valid         | Base Quote £7    | `10.01`, `14.99`    |
| BA5               | 15.01 to 20       | Valid         | Base Quote £10   | `15.01`, `19.99`    |
| BA6               | greater than 20   | Invalid       | Validation Error | `20.01`             |

### Decision Table

|                                    |   R1 |   R2 |
|------------------------------------|-----:|-----:|
| C1. Destination is Domestic        |    T |    F |
| C2. Destination is International   |    F |    T |
| ---------------------------------- | ---- | ---- |
| Base rate applied                  |    X |    X |
| International shipping added (20%) |      |    X |