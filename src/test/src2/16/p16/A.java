package p16;

public class A {

    void m(int x) {
        if (x < 0) {
            throw new AssertionError();
        }
    }
    
    void m2(Integer i) {
        if (i == null) {
            throw new NullPointerException();
        }
        System.out.println(i);
    }
}
