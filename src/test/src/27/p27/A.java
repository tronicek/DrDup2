package p27;

import java.awt.Point;

public class A {

    int x;

    void m1(Point p) {
        x = p.x;
    }

    void m2(Point p) {
        p.x = x;
    }
}
