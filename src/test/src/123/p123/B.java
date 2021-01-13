package p123;

public class B {

    public <T extends Number> void m(T t) {
        Object obj = t;
        System.out.println(obj);
    }
}
