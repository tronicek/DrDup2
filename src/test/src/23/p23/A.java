package p23;

public class A {

    Object make() {
        return new Object() {
            @Override
            public String toString() {
                return "hi";
            }
        };
    }
}
