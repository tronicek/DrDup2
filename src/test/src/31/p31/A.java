package p31;

import java.awt.Point;

public class A {

    Point p;

    void m1() {
        p.x = 0;
        p.y = 1;
    }

    void m2() {
        p.x = 2;
        p.x = 3;
    }
}
