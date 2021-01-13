package p111;

public class A {

    public A m() {
        Intf i = A::new;
        return i.create();
    }
}
