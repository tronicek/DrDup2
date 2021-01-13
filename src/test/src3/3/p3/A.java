package p3;

public class A {

    int read() {
        return 42;
    }

    void m(int x) {
        while (x > 0) {
            if (x % 3 == 1) {
                continue;
            }
            x--;
        }
    }

    void m2() {
        int y = read();
        while (y > 10) {
            if (y % 2 == 0) {
                continue;
            }
            y--;
        }
    }
}
