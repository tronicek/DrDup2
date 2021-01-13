package p110;

public class A {

    public Runnable[] make(A p) {
        return new Runnable[]{p::sayHi, p::println};
    }

    public void sayHi() {
        System.out.println("hi");
    }

    public void println() {
        System.out.println();
    }
}
