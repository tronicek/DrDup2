package p121;

public class A {

    public class X implements Runnable {

        @Override
        public void run() {
            String s = A.this.toString();
            A p = new A();
        }
    }
}
