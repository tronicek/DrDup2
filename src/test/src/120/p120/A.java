package p120;

public class A {

    public class X implements Runnable {

        @Override
        public void run() {
            String s = A.super.toString();
            A p = new A();
        }
    }
}
