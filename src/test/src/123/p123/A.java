package p123;

public class A {

    public <T extends Number> void m(T t) {
        Number n = t;
        System.out.println(n);
    }
}
