package p1;

import java.util.Collections;
import java.util.Set;

public class A {

    Set<?> createSet() {
        return Collections.<String>singleton(null);
    }

    Set<?> instantiateSet() {
        return Collections.singleton(null);
    }
}
