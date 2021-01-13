package p50;

public class A implements Runnable {

    Runnable create() {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("42");
            }
        };
    }

    @Override
    public void run() {
        System.out.println("hi");
    }
}
