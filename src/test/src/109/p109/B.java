package p109;

public class B {

    public Runnable[] make() {
        return new Runnable[]{B::sayHi, B::sayHi};
    }

    public static void sayHi() {
        System.out.println("sayHi begin");
        System.out.println("sayHi end");
    }
}
