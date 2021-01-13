package p110;

public class B {

    public Runnable[] make(B p) {
        return new Runnable[]{p::sayHi, p::sayHi};
    }

    public void sayHi() {
        System.out.println("sayHi begin");
        System.out.println("sayHi end");
    }
}
