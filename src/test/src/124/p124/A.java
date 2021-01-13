package p124;

import java.util.List;

public class A {

    public void m(List<? super Integer> p) {
        Integer i = 42;
        System.out.println(i);
    }
}
