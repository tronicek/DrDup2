package p97;

public class A {

    public Object makeRunnable() {
        class C1 extends Object { }
        class C2 extends C1 { }
        return new C1();
    }
}
