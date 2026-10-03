# Infix to Postfix

This is BIL-212 Data Structures Homework 1, completed on 21 January 2019.

The program reads a parenthesized expression whose operands are polynomials, and an integer value for `x`. Each polynomial is stored in a linked list. The expression is converted from infix to postfix with a stack, then evaluated, and the result is printed.

The assignment handout is `bil212summer2018hw1.pdf`.

## Run

```bash
javac src/InfixToPostfix.java
java -cp src InfixToPostfix
```

Enter the expression on the first line and the value of `x` on the second. Example:

```text
(3x^2 + 2x) * (5x^3 + 2)/((4x^15 + 3x^4 - 12x^2 + 2) + (4x))
2
```

For this input the program prints about `0.005126`.
