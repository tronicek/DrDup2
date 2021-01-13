package p98;

import java.io.Serializable;

public class B {

    public Intf makeRunnable() {
        class C1 implements Serializable, Intf { }
        return new C1();
    }
}
