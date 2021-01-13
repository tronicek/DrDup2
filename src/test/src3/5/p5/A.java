package p5;

public class A {

    void m(int x) {
        while (x > 0) {
            if (x > 1) {
                x++;
                System.out.println(x);
                x--;
            }
            x /= 2;
        }
    }

    void m2(int y) {
        for (int i = 0; i < 10; i++) {
            if (y > 1) {
                y++;
                System.out.println(y);
                y--;
            }
            y /= 2;
        }
    }
}
