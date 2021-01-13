package p92;

public class B {

    Runnable r = () -> {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        }.run();
    };
}
