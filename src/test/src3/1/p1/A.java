package p1;

public class A {

    void plus(int x) {
        assert x > 0;
        x++;
        if (x == 10) {
            System.out.print("hi");
        }
        minus(x);
    }

    void minus(int y) {
        assert y < 0;
        y++;
        if (y == 5) {
            System.out.print("hi");
        }
    }
}
