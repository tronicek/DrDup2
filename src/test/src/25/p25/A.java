package p25;

public class A {

    String s;
    int i;

    String make() {
        s = "";
        return s + i;
    }

    String concat() {
        i = 0;
        return i + s;
    }
}
