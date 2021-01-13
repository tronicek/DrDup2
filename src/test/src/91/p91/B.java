package p91;

public class B {

    static {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hi");
            }
        }.run();
    }
}
