package p124;

import java.util.List;

public class B {

    public void m(List<? super Integer> p) {
        Object obj = 42;
        System.out.println(obj);
    }
}
