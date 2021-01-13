package p98;

import java.io.Serializable;

public class A {

    public Serializable makeRunnable() {
        class C1 implements Serializable, Intf { }
        return new C1();
    }
}
