package p2;

public class A {

    void m(int x) {
        for (;;) {
            if (x > 0) {
                break;
            }
            x++;
        }
    }

    void m2(int y) {
        while (true) {
            if (y < 0) {
                break;
            }
            ++y;
        }
    }
}
