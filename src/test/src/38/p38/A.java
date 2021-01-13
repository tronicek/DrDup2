package p38;

import java.io.ObjectOutputStream;

public class A {

    void m1(ObjectOutputStream oos) throws Exception {
        oos.defaultWriteObject();
    }

    void m2(ObjectOutputStream oos) throws Exception {
        m1(oos);
    }
}
