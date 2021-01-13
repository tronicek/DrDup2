package p3;

public class A {

    void m(int x) {
        while (x > 0) {
            if (x == 5) {
                continue;
            }
            System.out.println(x);
            x--;
        }
    }
    
    void m2(int y) {
        while (y < 10) {
            if (y % 2 == 0) {
                continue;
            }
            y++;
        }
    }
}
