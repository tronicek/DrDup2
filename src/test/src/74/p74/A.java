package p74;

public class A {

    int div(int a, int b) {
        assert b != 0;
        return a / b;
    }

    int div2(int a, int b) {
        assert a != 0;
        return a / b;
    }
}
