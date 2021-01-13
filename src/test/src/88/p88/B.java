package p88;

public class B {

    Object[] p = {
        new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        }
    };
}
