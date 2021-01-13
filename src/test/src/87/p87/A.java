package p87;

public class A {

    Object[] p = {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hi");
            }
        }
    };
}
