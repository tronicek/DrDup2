package p2;

import java.util.function.Predicate;

public class A {

    Predicate<Integer> greaterThan0() {
        return i -> i > 0;
    }

    Predicate<Integer> positive() {
        return (j) -> (j > 0);
    }
}
