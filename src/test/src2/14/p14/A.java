package p14;

public class A {

    int toInt(String s) {
        switch (s) {
            case "M":
            case "L":
                return 40;
            default:
                throw new AssertionError();
        }
    }

    String toString(int size) {
        switch (size) {
            case 40:
            case 42:
                return "L";
            default:
                throw new AssertionError();
        }
    }
}
