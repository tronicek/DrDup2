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
            y /= 3;
        } while (y > 1);
        System.out.println(y);
    }

    void m3(int z) {
        do {
            z /= 4;
        } while (z > 2);
        System.out.println(z - 1);
    }
}
