package p107;

public class B {

    public int fib(int n) {
        if (n == 0 || n == 1) {
            return 1;
        }
        return fib2(n - 1) + fib2(n - 2);
    }

    private int fib2(int n) {
        return n;
    }
}
