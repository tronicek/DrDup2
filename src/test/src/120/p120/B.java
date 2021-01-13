package p120;

public class B {

    public class X implements Runnable {

        @Override
        public void run() {
            String s = B.super.toString();
            A p = new A();
        }
    }
}
