package p88;

public class A {

    Object[] p = new Runnable[] {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hi");
            }
        }
    };
}
