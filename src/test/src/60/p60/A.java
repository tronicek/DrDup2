package p60;

public class A {

    void m() {
        class MyRunnable implements Runnable {

            @Override
            public void run() {
                System.out.println("hi");
            }
        }
    }

    class MyRunnable implements Runnable {

        @Override
        public void run() {
            System.out.println("hey");
        }
    }
}
