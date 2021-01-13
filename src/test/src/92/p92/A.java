package p92;

public class A {

    Runnable r = () -> {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hi");
            }
        }.run();
    };
}
