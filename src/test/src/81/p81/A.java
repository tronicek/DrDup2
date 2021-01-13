package p81;

import java.util.Collection;

public class A {

    public void m() {
        @SuppressWarnings("checked")
        Collection c = null;
    }

    public void m2() {
        Collection c = null;
    }
}
