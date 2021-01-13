package p18;

public class A {

    int m(int x) {
        while (x > 0) {
            x /= 2;
        }
        return x;
    }

    void m2(int y) {
        while (y > 1) {
            y /= 2;
        }
        System.out.println(y);
    }
}
