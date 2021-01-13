package p47;

import java.util.ArrayList;
import java.util.List;

public class A {

    List<String> create() {
        return new ArrayList<>();
    }

    List<?> instantiate() {
        return new ArrayList<String>();
    }
}
