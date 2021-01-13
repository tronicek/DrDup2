package p109;

public class A {

    public Runnable[] make() {
        return new Runnable[]{A::sayHi, A::println};
    }

    public static void sayHi() {
        System.out.println("hi");
    }

    public static void println() {
        System.out.println();
    }
}
