package p9;

import java.util.List;
import java.util.Set;

public class A {

    void print(List<String> names) {
        for (String name : names) {
            System.out.println(name);
        }
    }
    
    void display(Set<String> cc) {
        for (String c : cc) {
            System.out.println(c);
        }
    }
}
