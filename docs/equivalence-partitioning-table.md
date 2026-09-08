| Equivalence Partition | Weight range           | Valid/Invalid | Expected result  | Core test condition  |
| --------------------- | ---------------------- | ------------- | ---------------- | -------------------- |
| EP1                   | `< 0 KG`               | Invalid       | Validation Error | Test negative weight |
| EP2                   | `= 0 KG`               | Invalid       | Validation Error | **0 KG**             |
| EP3                   | `> 0 KG and <= 5 KG`   | Valid         | Quote £3         | **5 KG**             |
| EP4                   | `> 5 KG and <= 10 KG`  | Valid         | Quote £5         | **10 KG**            |
| EP5                   | `> 10 KG and <= 15 KG` | Valid         | Quote £7         | **15 KG**            |
| EP6                   | `> 15 KG and <= 20 KG` | Valid         | Quote £10        | **20 KG**            |
| EP7                   | `> 20 KG`              | Invalid       | Validation Error | **>20 KG**           |
