package p73;

public class A {

    void m1(int x) {
        assert x > 0;
    }

    void m2(int x) {
        assert x > 0 : "x must be positive";
    }
}
