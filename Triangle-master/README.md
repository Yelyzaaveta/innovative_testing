# Lab 1 - Code Review

Orynchuk Yelyzaveta

Triangle.java refactoring

1. Empty first line of the file.

2. Fields `a, b, c` are not `final` even though they are immutable.

3. Method `takeA()`/`putA()` should be `getA()`/`setA()`

4. Method `takeB()`/`putB()` should be `getB()`/`setB()`

5. Method `takeC()`/`putC()` should be `getC()`/`setC()`

6. `putA()`/`putB()`/`putC()` do not check that the side is positive - a negative or zero value can be set

7. Constructor `Triangle(a, b, c)` does not check the triangle inequality, which allows creating a "triangle" with invalid sides

8. Empty constructor `Triangle()` leaves the object with all sides equal to 0.0

9. Method `perim()` has a 3-space indent instead of 4

10. `area()` also has a 3-space indent instead of 4

11. `area()` uses the magic number `0.5` directly in the formula 4 times, it's unclear what this number means

12. `area()` recomputes `0.5*perim()` four times instead of storing the result in a single variable

13. `area()` will return `NaN` for an invalid triangle

14. `area()`: `0.5*perim()*(0.5*perim()-a)*(0.5*perim()-b)*(0.5*perim()-c)` - no spaces around the `*` and `-` operators, hard to read the formula

15. `equilateral()` also has a 3-space indent instead of 4

16. `public  boolean  equilateral()` - contains double spaces

17. `equilateral(){` - no space before the opening curly brace

18. `equilateral()` uses a redundant if-else construction that returns boolean literals, instead of `return a == b && b == c;`

19. `equilateral()` has an extra blank line before the closing brace `}`

20. `toString()` looks strange - broken by line wraps into a bunch of lines instead of one compact variant:
```java
public String toString() {
    return "Triangle{" +
            "a=" + a +
            ", b=" + b +
            ", c=" + c +
            '}';
}
```
Better: `return String.format("Triangle{a=%s, b=%s, c=%s}", a, b, c);`

21. Method `equals()` is marked `final`, why?

22. The name of the method `equals()` is ambiguous as a boolean method - it's unclear "who is compared with whom". `isEqualTo(Object o)` would be better.

23. Parameter `o` in `equals(Object o)` has too short, unclear a name

24. `equals()` compares sides strictly by position, but in a different order they would be considered unequal, even though geometrically it's the same triangle

25. `hashCode()` contains the magic number `31` with no explanation of what this number is and why it's used

26. `hashCode()` could be simplified using a method instead of manual computation.

27. The name of the method `perim()` is an abbreviation, inconsistent with the rest of the methods, which use full words (`area()`, `equilateral()`) - `perimeter()` would be better

28. The name of the method `equilateral()` does not follow the boolean method naming convention - `isEquilateral()` would be better
