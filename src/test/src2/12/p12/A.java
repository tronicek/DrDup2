package p12;

public class A {

    void m(int n) {
        loop:
        for (int i = 0; i < n; i++) {
            System.out.println(i);
        }
    }

    void m2(int m) {
        m++;
        lab:
        for (int i = 1; i < m; i++) {
            System.out.println(i);
        }
    }
}
