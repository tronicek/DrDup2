package p91;

public class A {

    static {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hi");
            }
        }.run();
    }
}
