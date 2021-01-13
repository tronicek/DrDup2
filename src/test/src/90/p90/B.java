package p90;

public class B {

    {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hi");
            }
        }.run();
    }
}
