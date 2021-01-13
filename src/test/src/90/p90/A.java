package p90;

public class A {

    {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hi");
            }
        }.run();
    }
}
