package p97;

public class B {

    public Object makeRunnable() {
        class C1 extends Object { }
        class C2 extends C1 { }
        return new C2();
    }
}
