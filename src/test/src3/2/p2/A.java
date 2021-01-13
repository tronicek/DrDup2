package p2;

public class A {

    void m(int x) {
        for (;;) {
            if (x > 0) {
                break;
            }
            x++;
        }
        System.out.println("hi");
    }

    void m2(int y) {
        int x = 10;
        for ( ; ; ) {
            if (y > 0) {
                break;
            }
            y++;
        }
        m(x);
    }
}
