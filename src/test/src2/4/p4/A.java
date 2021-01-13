package p4;

public class A {

    void m1(int x) {
        do {
            x /= 2;
        } while (x > 0);
        System.out.println(x + 1);
    }

    void m2(int y) {
        do {
            y /= 2;
        } while (y > 0);
        System.out.println(y);
    }
}
