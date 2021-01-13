package p33;

import java.awt.Point;

public class A {

    static Point p;

    static void m1() {
        p.x = 0;
        p.y = 1;
    }

    static void m2() {
        p.x = 2;
        p.x = 3;
    }
}
